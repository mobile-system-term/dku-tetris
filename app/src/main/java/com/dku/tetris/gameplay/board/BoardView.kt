package com.dku.tetris.gameplay.board

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log
import android.view.GestureDetector;
import android.view.KeyEvent
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.ContextCompat;


import java.util.Timer;
import java.util.TimerTask;

import androidx.core.content.withStyledAttributes
import com.dku.tetris.R
import com.dku.tetris.gameplay.piece.PieceType
import com.dku.tetris.settings.dao.SettingDAO

class BoardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private val boardPaint = Paint()
    private val borderPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 5f
        color = Color.BLACK
    }

    fun steerLeft() = board.steer(-1)
    fun steerRight() = board.steer(1)
    fun rotate() = board.rotate(1)

    private var rows = 20
    private var columns = 10
    private var unit = 0f
    private var backgroundColor = Color.BLACK

    private lateinit var board: Board
    private lateinit var timer: Timer
    private lateinit var handler: Handler

    init {
        init(context, attrs, defStyle)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyle: Int) {
        context.withStyledAttributes(attrs, R.styleable.BoardView, defStyle, 0) {

            rows = getInt(R.styleable.BoardView_rows, 20)
            columns = getInt(R.styleable.BoardView_columns, 10)
            backgroundColor = getColor(R.styleable.BoardView_backgroundColor, Color.BLACK)

            val settings = SettingDAO.getSetting(context)
            columns = settings!!.width
            rows = settings.height
            val levels: MutableList<MutableList<Int>>? = settings.timeLevels

            board = Board(rows, columns, levels)
            board.startGame()

            timer = Timer()
            handler = Handler(context.mainLooper) {
                invalidate()
                true
            }

            timer.schedule(object : TimerTask() {
                override fun run() {
                    handler.sendMessage(handler.obtainMessage())
                }
            }, 1000, 100)
        }
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        super.onMeasure(widthSpec, heightSpec)
        val width = measuredWidth
        val height = measuredHeight
        unit = minOf(width.toFloat() / columns, height.toFloat() / rows)
        setMeasuredDimension((unit * columns).toInt(), (unit * rows).toInt())
    }

    fun drawUnit(canvas: Canvas, x: Float, y: Float, pad: Float, paint: Paint) {
        val realX = x * unit
        val realY = y * unit
        val r = unit / 5
        val rect = RectF(realY + pad, realX + pad, realY + unit - pad, realX + unit - pad)
        canvas.drawRoundRect(rect, r, r, paint)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), boardPaint.apply {
            color = backgroundColor
        })

        val piece = board.getCurrentPiece()
        boardPaint.color = ContextCompat.getColor(context, piece!!.color)
        for (coord in piece.getBlockPositions()) {
            drawUnit(canvas, coord.x.toFloat (), coord.y.toFloat(), 0f, boardPaint)
            drawUnit(canvas, coord.x.toFloat(), coord.y.toFloat(), -1f, borderPaint)
        }

        val pile = board.getPile()
        for (i in pile.indices) {
            for (j in pile[i].indices) {
                if (pile[i][j] != 0) {
                    boardPaint.color = ContextCompat.getColor(context, pile[i][j])
                    drawUnit(canvas, i.toFloat(), j.toFloat(), 0f, boardPaint)
                    drawUnit(canvas, i.toFloat(), j.toFloat(), 2f, borderPaint)
                }
            }
        }
    }

    fun pause() {
        board.pause()
    }

    fun hold(): PieceType? = board.hold()

    fun setOnNextPieceListener(listener: Board.OnNextPieceListener) {
        board.nextPieceListener = listener
    }

    fun setOnLineClearListener(listener: Board.OnLineClearListener) {
        board.lineClearListener = listener
    }

    fun setOnGameOverListener(listener: Board.OnGameOverListener) {
        board.gameOverListener = listener
    }

    fun setLineClearScore(score: Int) {
        board.setLineClearScore(score)
    }
}