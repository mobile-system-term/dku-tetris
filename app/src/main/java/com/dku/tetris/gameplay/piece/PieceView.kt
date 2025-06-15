package com.dku.tetris.gameplay.piece

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class PieceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var nextPiece: PieceType? = null

    private var unit = 0f
    private val paint = Paint()
    private val borderPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f
        color = Color.BLACK
    }

    private var canvasRef: Canvas? = null
    private var mw = 0f
    private var mh = 0f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val piece = nextPiece ?: return

        unit = minOf(width / 4f, height / 2f)
        mw = (width - unit * piece.width) / 2
        mh = (height - unit * piece.height) / 2
        canvasRef = canvas
        paint.color = piece.color

        for (n in piece.getData()) {
            val x = if (n < 4) 0 else 1
            val y = n % 4
            drawUnit(x.toFloat(), y.toFloat(), 0f, paint)
            drawUnit(x.toFloat(), y.toFloat(), -1f, borderPaint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val w = measuredWidth
        setMeasuredDimension(w, w)
    }

    private fun drawUnit(xOrig: Float, yOrig: Float, pad: Float, paint: Paint) {
        val x = xOrig * unit + mh
        val y = yOrig * unit + mw
        val r = unit / 5
        val rF = RectF(y + pad, x + pad, y + unit - pad, x + unit - pad)
        canvasRef?.drawRoundRect(rF, r, r, paint)
    }

    fun setPiece(piece: PieceType) {
        nextPiece = piece
        invalidate()
    }
}
