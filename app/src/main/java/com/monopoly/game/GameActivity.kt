package com.monopoly.game

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.monopoly.game.adapter.PlayerInfoAdapter
import com.monopoly.game.databinding.ActivityGameBinding
import com.monopoly.game.model.BoardSquare
import com.monopoly.game.model.Property

class GameActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityGameBinding
    private lateinit var gameEngine: GameEngine
    private lateinit var playerInfoAdapter: PlayerInfoAdapter
    private var currentProperty: Property? = null
    
    private val diceImages = listOf(
        R.drawable.dice_1, R.drawable.dice_2, R.drawable.dice_3,
        R.drawable.dice_4, R.drawable.dice_5, R.drawable.dice_6
    )
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        initGame()
        setupUI()
        setupListeners()
        updateUI()
    }
    
    private fun initGame() {
        gameEngine = GameEngine()
        
        val playerNames = intent.getStringArrayListExtra("player_names") ?: arrayListOf()
        if (playerNames.isEmpty()) {
            finish()
            return
        }
        
        gameEngine.initGame(playerNames)
    }
    
    private fun setupUI() {
        // 设置棋盘视图
        binding.boardView.setGameBoard(gameEngine.getBoard(), gameEngine.getAllPlayers())
        
        // 设置玩家信息RecyclerView
        playerInfoAdapter = PlayerInfoAdapter(gameEngine.getAllPlayers())
        binding.rvPlayerInfo.apply {
            layoutManager = LinearLayoutManager(this@GameActivity)
            adapter = playerInfoAdapter
        }
    }
    
    private fun setupListeners() {
        binding.btnRollDice.setOnClickListener {
            rollDiceAndMove()
        }
        
        binding.btnEndTurn.setOnClickListener {
            endTurn()
        }
        
        binding.btnBuyProperty.setOnClickListener {
            buyProperty()
        }
        
        binding.btnBuildHouse.setOnClickListener {
            buildHouse()
        }
        
        binding.btnBuildHotel.setOnClickListener {
            buildHotel()
        }
    }
    
    private fun rollDiceAndMove() {
        val currentPlayer = gameEngine.getCurrentPlayer()
        
        if (currentPlayer.isInJail) {
            handleJailRoll()
            return
        }
        
        // 掷骰子动画
        val dice = gameEngine.rollDice()
        animateDice(dice) {
            // 移动玩家
            val moveResult = gameEngine.movePlayer(dice.first + dice.second)
            
            // 更新界面
            binding.boardView.setGameBoard(gameEngine.getBoard(), gameEngine.getAllPlayers())
            
            // 显示移动信息
            if (moveResult.passedStart) {
                showMessage("经过起点，获得 $200")
            }
            
            // 处理落地事件
            val actionResult = gameEngine.handleSquareAction(moveResult.landedSquare)
            handleSquareAction(actionResult, moveResult.landedSquare)
            
            // 更新UI
            updateUI()
            
            // 启用结束回合按钮
            binding.btnEndTurn.isEnabled = true
            binding.btnRollDice.isEnabled = false
        }
    }
    
    private fun animateDice(dice: Pair<Int, Int>, onComplete: () -> Unit) {
        val handler = Handler(Looper.getMainLooper())
        var animationCount = 0
        val maxAnimations = 10
        
        val animationRunnable = object : Runnable {
            override fun run() {
                if (animationCount < maxAnimations) {
                    // 随机显示骰子
                    val randomDice1 = (1..6).random()
                    val randomDice2 = (1..6).random()
                    
                    binding.ivDice1.setImageResource(diceImages[randomDice1 - 1])
                    binding.ivDice2.setImageResource(diceImages[randomDice2 - 1])
                    
                    animationCount++
                    handler.postDelayed(this, 100)
                } else {
                    // 显示最终结果
                    binding.ivDice1.setImageResource(diceImages[dice.first - 1])
                    binding.ivDice2.setImageResource(diceImages[dice.second - 1])
                    binding.tvDiceResult.text = getString(R.string.dice_result, dice.first, dice.second, dice.first + dice.second)
                    
                    onComplete()
                }
            }
        }
        
        handler.post(animationRunnable)
    }
    
    private fun handleJailRoll() {
        val dice = gameEngine.rollDice()
        binding.ivDice1.setImageResource(diceImages[dice.first - 1])
        binding.ivDice2.setImageResource(diceImages[dice.second - 1])
        
        val currentPlayer = gameEngine.getCurrentPlayer()
        
        if (dice.first == dice.second) {
            // 掷出相同点数，出狱
            currentPlayer.isInJail = false
            currentPlayer.jailTurns = 0
            showMessage("掷出相同点数，获得自由！")
            
            val moveResult = gameEngine.movePlayer(dice.first + dice.second)
            binding.boardView.setGameBoard(gameEngine.getBoard(), gameEngine.getAllPlayers())
            
            val actionResult = gameEngine.handleSquareAction(moveResult.landedSquare)
            handleSquareAction(actionResult, moveResult.landedSquare)
        } else {
            showMessage("未能出狱，回合结束")
            endTurn()
        }
        
        updateUI()
    }
    
    private fun handleSquareAction(actionResult: SquareActionResult, square: BoardSquare) {
        when (actionResult) {
            is SquareActionResult.PurchaseOption -> {
                currentProperty = actionResult.property
                showPropertyPurchaseDialog(actionResult.property)
            }
            is SquareActionResult.OwnProperty -> {
                currentProperty = actionResult.property
                showPropertyManageDialog(actionResult.property)
            }
            is SquareActionResult.PayRent -> {
                if (actionResult.successful) {
                    showMessage("向 ${actionResult.property.owner?.name} 支付租金 $${actionResult.amount}")
                } else {
                    showMessage("资金不足，无法支付租金！")
                    handleBankruptcy()
                }
            }
            is SquareActionResult.PayTax -> {
                if (actionResult.successful) {
                    showMessage("缴纳税款 $${actionResult.amount}")
                } else {
                    showMessage("资金不足，无法缴税！")
                    handleBankruptcy()
                }
            }
            is SquareActionResult.ChanceCard,
            is SquareActionResult.CommunityCard -> {
                val message = when (actionResult) {
                    is SquareActionResult.ChanceCard -> actionResult.message
                    is SquareActionResult.CommunityCard -> actionResult.message
                    else -> ""
                }
                showCardDialog(message)
            }
            is SquareActionResult.GoToJail -> {
                showMessage("进入监狱！")
            }
            else -> {
                // 其他特殊格子不需要特别处理
            }
        }
    }
    
    private fun showPropertyPurchaseDialog(property: Property) {
        binding.actionPanel.visibility = View.VISIBLE
        binding.tvActionTitle.text = property.name
        binding.tvActionDescription.text = "价格: $${property.price}\n租金: $${property.rent}"
        binding.btnBuyProperty.visibility = View.VISIBLE
        binding.btnBuildHouse.visibility = View.GONE
        binding.btnBuildHotel.visibility = View.GONE
    }
    
    private fun showPropertyManageDialog(property: Property) {
        binding.actionPanel.visibility = View.VISIBLE
        binding.tvActionTitle.text = property.name
        binding.tvActionDescription.text = "当前租金: $${property.getCurrentRent()}"
        binding.btnBuyProperty.visibility = View.GONE
        binding.btnBuildHouse.visibility = if (property.canBuildHouse()) View.VISIBLE else View.GONE
        binding.btnBuildHotel.visibility = if (property.canBuildHotel()) View.VISIBLE else View.GONE
    }
    
    private fun showCardDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("机会/命运")
            .setMessage(message)
            .setPositiveButton("确定") { _, _ -> }
            .show()
    }
    
    private fun buyProperty() {
        currentProperty?.let { property ->
            if (gameEngine.purchaseProperty(property)) {
                showMessage("成功购买 ${property.name}")
                playerInfoAdapter.notifyDataSetChanged()
            } else {
                showMessage("资金不足，无法购买")
            }
        }
        hideActionPanel()
    }
    
    private fun buildHouse() {
        currentProperty?.let { property ->
            if (gameEngine.buildHouse(property)) {
                showMessage("在 ${property.name} 建造了房屋")
                binding.boardView.setGameBoard(gameEngine.getBoard(), gameEngine.getAllPlayers())
                playerInfoAdapter.notifyDataSetChanged()
            } else {
                showMessage("无法建造房屋")
            }
        }
        hideActionPanel()
    }
    
    private fun buildHotel() {
        currentProperty?.let { property ->
            if (gameEngine.buildHotel(property)) {
                showMessage("在 ${property.name} 建造了酒店")
                binding.boardView.setGameBoard(gameEngine.getBoard(), gameEngine.getAllPlayers())
                playerInfoAdapter.notifyDataSetChanged()
            } else {
                showMessage("无法建造酒店")
            }
        }
        hideActionPanel()
    }
    
    private fun hideActionPanel() {
        binding.actionPanel.visibility = View.GONE
        currentProperty = null
    }
    
    private fun endTurn() {
        hideActionPanel()
        
        // 检查破产
        val currentPlayer = gameEngine.getCurrentPlayer()
        if (gameEngine.checkBankruptcy(currentPlayer)) {
            showMessage("${currentPlayer.name} 破产了！")
        }
        
        // 下一回合
        gameEngine.nextTurn()
        
        // 检查游戏结束
        if (gameEngine.getGameState() == GameState.FINISHED) {
            showGameOverDialog()
            return
        }
        
        // 更新UI
        updateUI()
        
        // 重置按钮状态
        binding.btnRollDice.isEnabled = true
        binding.btnEndTurn.isEnabled = false
        binding.tvDiceResult.text = ""
    }
    
    private fun handleBankruptcy() {
        val currentPlayer = gameEngine.getCurrentPlayer()
        
        AlertDialog.Builder(this)
            .setTitle("破产警告")
            .setMessage("${currentPlayer.name} 资金不足！需要出售房产或宣布破产。")
            .setPositiveButton("宣布破产") { _, _ ->
                gameEngine.checkBankruptcy(currentPlayer)
                endTurn()
            }
            .setNegativeButton("继续游戏", null)
            .show()
    }
    
    private fun showGameOverDialog() {
        val winner = gameEngine.getAllPlayers().find { !it.isBankrupt }
        val message = if (winner != null) {
            "游戏结束！\n获胜者: ${winner.name}\n总资产: $${winner.getTotalAssets()}"
        } else {
            "游戏结束！"
        }
        
        AlertDialog.Builder(this)
            .setTitle("游戏结束")
            .setMessage(message)
            .setPositiveButton("重新开始") { _, _ ->
                finish()
            }
            .setNegativeButton("退出") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()
    }
    
    private fun updateUI() {
        val currentPlayer = gameEngine.getCurrentPlayer()
        
        binding.tvCurrentPlayer.text = getString(R.string.current_player, currentPlayer.name)
        binding.tvPlayerMoney.text = getString(R.string.player_money, currentPlayer.money)
        
        if (currentPlayer.isInJail) {
            binding.tvCurrentPlayer.text = "${currentPlayer.name} (在监狱中)"
        }
        
        playerInfoAdapter.notifyDataSetChanged()
    }
    
    private fun showMessage(message: String) {
        binding.tvGameMessage.text = message
        binding.tvGameMessage.visibility = View.VISIBLE
        
        Handler(Looper.getMainLooper()).postDelayed({
            binding.tvGameMessage.visibility = View.GONE
        }, 3000)
        
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}