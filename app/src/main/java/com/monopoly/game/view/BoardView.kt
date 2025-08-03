package com.monopoly.game.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.monopoly.game.R
import com.monopoly.game.model.*

class BoardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var gameBoard: GameBoard? = null
    private var players: List<Player> = emptyList()
    
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val playerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    
    private val squareSize = 60f
    private val cornerSize = 80f
    private val playerTokenSize = 12f
    
    init {
        textPaint.textSize = 24f
        textPaint.color = Color.BLACK
        textPaint.textAlign = Paint.Align.CENTER
        
        playerPaint.style = Paint.Style.FILL
    }
    
    fun setGameBoard(board: GameBoard, playerList: List<Player>) {
        gameBoard = board
        players = playerList
        invalidate()
    }
    
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas ?: return
        
        val board = gameBoard ?: return
        
        drawBoard(canvas)
        drawSquares(canvas, board)
        drawPlayers(canvas)
    }
    
    private fun drawBoard(canvas: Canvas) {
        // 绘制棋盘边框
        paint.color = context.getColor(R.color.square_border)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 8f
        
        val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
        canvas.drawRect(rect, paint)
        
        // 绘制中央区域
        paint.style = Paint.Style.FILL
        paint.color = context.getColor(R.color.white)
        
        val centerRect = RectF(
            cornerSize + squareSize,
            cornerSize + squareSize,
            width - cornerSize - squareSize,
            height - cornerSize - squareSize
        )
        canvas.drawRect(centerRect, paint)
    }
    
    private fun drawSquares(canvas: Canvas, board: GameBoard) {
        board.squares.forEachIndexed { index, square ->
            drawSquare(canvas, square, index)
        }
    }
    
    private fun drawSquare(canvas: Canvas, square: BoardSquare, position: Int) {
        val rect = getSquareRect(position)
        
        // 绘制格子背景
        paint.style = Paint.Style.FILL
        paint.color = getSquareColor(square)
        canvas.drawRect(rect, paint)
        
        // 绘制格子边框
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f
        paint.color = context.getColor(R.color.square_border)
        canvas.drawRect(rect, paint)
        
        // 绘制格子文字
        drawSquareText(canvas, square, rect)
        
        // 绘制房屋/酒店标识
        if (square is BoardSquare.PropertySquare) {
            drawPropertyImprovements(canvas, square.property, rect)
        }
    }
    
    private fun getSquareRect(position: Int): RectF {
        return when {
            position == 0 -> RectF(0f, height - cornerSize, cornerSize, height.toFloat()) // 起点
            position <= 9 -> { // 右侧
                val x = cornerSize + (position - 1) * squareSize
                RectF(x, height - squareSize, x + squareSize, height.toFloat())
            }
            position == 10 -> RectF(width - cornerSize, height - cornerSize, width.toFloat(), height.toFloat()) // 监狱
            position <= 19 -> { // 上侧
                val x = width - cornerSize - (position - 10) * squareSize
                RectF(x - squareSize, 0f, x, squareSize)
            }
            position == 20 -> RectF(0f, 0f, cornerSize, cornerSize) // 免费停车
            position <= 29 -> { // 左侧
                val y = cornerSize + (position - 21) * squareSize
                RectF(0f, y, squareSize, y + squareSize)
            }
            position == 30 -> RectF(width - cornerSize, 0f, width.toFloat(), cornerSize) // 进监狱
            else -> { // 下侧
                val x = cornerSize + (position - 31) * squareSize
                RectF(x, height - squareSize, x + squareSize, height.toFloat())
            }
        }
    }
    
    private fun getSquareColor(square: BoardSquare): Int {
        return when (square) {
            is BoardSquare.PropertySquare -> getPropertyColor(square.property.color)
            is BoardSquare.SpecialSquare -> context.getColor(R.color.white)
            else -> context.getColor(R.color.white)
        }
    }
    
    private fun getPropertyColor(color: PropertyColor): Int {
        return when (color) {
            PropertyColor.BROWN -> context.getColor(R.color.property_brown)
            PropertyColor.LIGHT_BLUE -> context.getColor(R.color.property_light_blue)
            PropertyColor.PINK -> context.getColor(R.color.property_pink)
            PropertyColor.ORANGE -> context.getColor(R.color.property_orange)
            PropertyColor.RED -> context.getColor(R.color.property_red)
            PropertyColor.YELLOW -> context.getColor(R.color.property_yellow)
            PropertyColor.GREEN -> context.getColor(R.color.property_green)
            PropertyColor.DARK_BLUE -> context.getColor(R.color.property_dark_blue)
            PropertyColor.UTILITY -> context.getColor(R.color.property_utility)
            PropertyColor.RAILROAD -> context.getColor(R.color.property_railroad)
        }
    }
    
    private fun drawSquareText(canvas: Canvas, square: BoardSquare, rect: RectF) {
        val text = square.name
        val centerX = rect.centerX()
        val centerY = rect.centerY()
        
        textPaint.textSize = 16f
        textPaint.color = Color.BLACK
        
        // 根据文字长度调整字体大小
        val textWidth = textPaint.measureText(text)
        if (textWidth > rect.width() - 8) {
            textPaint.textSize = 12f
        }
        
        canvas.drawText(text, centerX, centerY + textPaint.textSize / 3, textPaint)
    }
    
    private fun drawPropertyImprovements(canvas: Canvas, property: Property, rect: RectF) {
        if (property.hasHouse || property.hasHotel) {
            paint.style = Paint.Style.FILL
            paint.color = if (property.hasHotel) Color.RED else Color.GREEN
            
            val improvementSize = 8f
            val x = rect.right - improvementSize - 4f
            val y = rect.top + 4f
            
            canvas.drawRect(x, y, x + improvementSize, y + improvementSize, paint)
        }
    }
    
    private fun drawPlayers(canvas: Canvas) {
        players.forEach { player ->
            drawPlayer(canvas, player)
        }
    }
    
    private fun drawPlayer(canvas: Canvas, player: Player) {
        val rect = getSquareRect(player.position)
        
        playerPaint.color = player.color
        
        // 计算玩家在格子中的位置（支持多个玩家在同一格）
        val playersAtPosition = players.filter { it.position == player.position }
        val playerIndex = playersAtPosition.indexOf(player)
        val totalPlayers = playersAtPosition.size
        
        val offsetX = (playerIndex % 2) * playerTokenSize
        val offsetY = (playerIndex / 2) * playerTokenSize
        
        val centerX = rect.centerX() - playerTokenSize + offsetX
        val centerY = rect.centerY() - playerTokenSize + offsetY
        
        canvas.drawCircle(centerX, centerY, playerTokenSize / 2, playerPaint)
        
        // 绘制玩家边框
        playerPaint.color = Color.WHITE
        playerPaint.style = Paint.Style.STROKE
        playerPaint.strokeWidth = 2f
        canvas.drawCircle(centerX, centerY, playerTokenSize / 2, playerPaint)
        playerPaint.style = Paint.Style.FILL
    }
}