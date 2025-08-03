package com.monopoly.game

import com.monopoly.game.model.*
import kotlin.random.Random

class GameEngine {
    private val board = GameBoard()
    private val players = mutableListOf<Player>()
    private var currentPlayerIndex = 0
    private var gameState = GameState.SETUP
    
    fun initGame(playerNames: List<String>) {
        players.clear()
        val colors = listOf(
            android.graphics.Color.RED,
            android.graphics.Color.BLUE,
            android.graphics.Color.GREEN,
            android.graphics.Color.YELLOW
        )
        
        playerNames.forEachIndexed { index, name ->
            players.add(Player(index, name, colors[index % colors.size]))
        }
        
        currentPlayerIndex = 0
        gameState = GameState.PLAYING
    }
    
    fun getCurrentPlayer(): Player = players[currentPlayerIndex]
    
    fun getAllPlayers(): List<Player> = players.toList()
    
    fun rollDice(): Pair<Int, Int> {
        val dice1 = Random.nextInt(1, 7)
        val dice2 = Random.nextInt(1, 7)
        return Pair(dice1, dice2)
    }
    
    fun movePlayer(diceRoll: Int): MoveResult {
        val player = getCurrentPlayer()
        val oldPosition = player.position
        val newPosition = (oldPosition + diceRoll) % 40
        
        // 检查是否过了起点
        val passedStart = newPosition < oldPosition
        if (passedStart) {
            player.addMoney(200) // 过起点奖励200
        }
        
        player.position = newPosition
        val square = board.getSquare(newPosition)
        
        return MoveResult(
            player = player,
            fromPosition = oldPosition,
            toPosition = newPosition,
            passedStart = passedStart,
            landedSquare = square
        )
    }
    
    fun handleSquareAction(square: BoardSquare): SquareActionResult {
        val player = getCurrentPlayer()
        
        return when (square) {
            is BoardSquare.PropertySquare -> handlePropertySquare(player, square.property)
            is BoardSquare.SpecialSquare -> handleSpecialSquare(player, square)
            is BoardSquare.ChanceSquare -> handleChanceSquare(player)
            is BoardSquare.CommunityChestSquare -> handleCommunityChestSquare(player)
        }
    }
    
    private fun handlePropertySquare(player: Player, property: Property): SquareActionResult {
        return when {
            property.owner == null -> {
                // 无主房产，可以购买
                SquareActionResult.PurchaseOption(property)
            }
            property.owner == player -> {
                // 自己的房产，可以建造房屋
                SquareActionResult.OwnProperty(property)
            }
            else -> {
                // 别人的房产，需要付租金
                val rent = property.getCurrentRent()
                if (player.removeMoney(rent)) {
                    property.owner!!.addMoney(rent)
                    SquareActionResult.PayRent(property, rent, true)
                } else {
                    SquareActionResult.PayRent(property, rent, false)
                }
            }
        }
    }
    
    private fun handleSpecialSquare(player: Player, square: BoardSquare.SpecialSquare): SquareActionResult {
        return when (square.type) {
            SpecialType.START -> SquareActionResult.StartSquare
            SpecialType.JAIL -> SquareActionResult.JailVisit
            SpecialType.FREE_PARKING -> SquareActionResult.FreeParking
            SpecialType.GO_TO_JAIL -> {
                player.position = 10 // 监狱位置
                player.isInJail = true
                player.jailTurns = 0
                SquareActionResult.GoToJail
            }
            SpecialType.TAX -> {
                val taxAmount = if (square.name.contains("所得税")) 200 else 100
                if (player.removeMoney(taxAmount)) {
                    SquareActionResult.PayTax(taxAmount, true)
                } else {
                    SquareActionResult.PayTax(taxAmount, false)
                }
            }
        }
    }
    
    private fun handleChanceSquare(player: Player): SquareActionResult {
        val chanceCards = listOf(
            "前进到起点，获得200元",
            "前进到日月潭",
            "获得银行红利200元",
            "缴纳150元罚款",
            "获得100元生日红包",
            "前进到监狱"
        )
        
        val card = chanceCards.random()
        return SquareActionResult.ChanceCard(card)
    }
    
    private fun handleCommunityChestSquare(player: Player): SquareActionResult {
        val communityCards = listOf(
            "获得遗产继承100元",
            "医疗费用缴纳50元",
            "获得税务退款100元",
            "缴纳路费修缮50元",
            "获得投资收益150元",
            "前进到起点"
        )
        
        val card = communityCards.random()
        return SquareActionResult.CommunityCard(card)
    }
    
    fun purchaseProperty(property: Property): Boolean {
        val player = getCurrentPlayer()
        return if (player.canAfford(property.price) && property.owner == null) {
            player.removeMoney(property.price)
            player.addProperty(property)
            true
        } else {
            false
        }
    }
    
    fun buildHouse(property: Property): Boolean {
        val player = getCurrentPlayer()
        val houseCost = property.price / 2
        
        return if (property.owner == player && 
                   player.canAfford(houseCost) && 
                   property.canBuildHouse()) {
            player.removeMoney(houseCost)
            property.buildHouse()
            true
        } else {
            false
        }
    }
    
    fun buildHotel(property: Property): Boolean {
        val player = getCurrentPlayer()
        val hotelCost = property.price
        
        return if (property.owner == player && 
                   player.canAfford(hotelCost) && 
                   property.canBuildHotel()) {
            player.removeMoney(hotelCost)
            property.buildHotel()
            true
        } else {
            false
        }
    }
    
    fun nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size
        
        // 检查监狱状态
        val currentPlayer = getCurrentPlayer()
        if (currentPlayer.isInJail) {
            currentPlayer.jailTurns++
            if (currentPlayer.jailTurns >= 3) {
                currentPlayer.isInJail = false
                currentPlayer.jailTurns = 0
            }
        }
        
        // 检查游戏结束条件
        val activePlayers = players.filter { !it.isBankrupt }
        if (activePlayers.size <= 1) {
            gameState = GameState.FINISHED
        }
    }
    
    fun checkBankruptcy(player: Player): Boolean {
        if (player.money < 0 && player.getTotalAssets() < Math.abs(player.money)) {
            player.isBankrupt = true
            // 释放所有房产
            player.ownedProperties.forEach { property ->
                property.owner = null
                property.hasHouse = false
                property.hasHotel = false
            }
            player.ownedProperties.clear()
            return true
        }
        return false
    }
    
    fun getGameState(): GameState = gameState
    
    fun getBoard(): GameBoard = board
}

enum class GameState {
    SETUP, PLAYING, FINISHED
}

data class MoveResult(
    val player: Player,
    val fromPosition: Int,
    val toPosition: Int,
    val passedStart: Boolean,
    val landedSquare: BoardSquare
)

sealed class SquareActionResult {
    object StartSquare : SquareActionResult()
    object JailVisit : SquareActionResult()
    object FreeParking : SquareActionResult()
    object GoToJail : SquareActionResult()
    
    data class PurchaseOption(val property: Property) : SquareActionResult()
    data class OwnProperty(val property: Property) : SquareActionResult()
    data class PayRent(val property: Property, val amount: Int, val successful: Boolean) : SquareActionResult()
    data class PayTax(val amount: Int, val successful: Boolean) : SquareActionResult()
    data class ChanceCard(val message: String) : SquareActionResult()
    data class CommunityCard(val message: String) : SquareActionResult()
}