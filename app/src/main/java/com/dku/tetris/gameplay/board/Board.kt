package com.dku.tetris.gameplay.board

import com.dku.tetris.gameplay.piece.PieceType

import android.os.Looper;
import android.os.Message;
import android.os.Handler
import android.util.Log;
import android.util.Pair;
import com.dku.tetris.gameplay.piece.Piece

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;




class Board(val rows: Int, val columns: Int, levels: MutableList<MutableList<Int>>?) {
    interface OnNextPieceListener {
        fun onNewNextPiece(nextPiece: PieceType)
    }

    interface OnLineClearListener {
        fun onLineClear(score: Int)
    }

    interface OnGameOverListener {
        fun onGameOver()
    }

    companion object {
        val pieceTypes = PieceType.entries.toTypedArray()
    }

    private val rng = Random()
    private val timer: BoardTimer

    private var lineClearScore = 800
    private val pile: Array<IntArray> = Array(rows) { IntArray(columns) }

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            tick()
        }
    }

    var nextPieceListener: OnNextPieceListener? = null
    var lineClearListener: OnLineClearListener? = null
    var gameOverListener: OnGameOverListener? = null

    private val queue: Queue<Piece> = LinkedList()
    private var currentPiece: Piece? = null
    private var holdPiece: Piece? = null

    init {
        if (columns < 4)
            throw IllegalArgumentException("Board needs at least 4 columns")

        val levelsQueue: Queue<Pair<Int, Int>> = LinkedList()
        if (levels != null) {
            for (level in levels)
                levelsQueue.add(Pair(level[0], level[1]))
        }

        timer = BoardTimer(handler, levelsQueue)

        generateNewPiece()
        generateNewPiece()
        currentPiece = queue.poll()
        nextPieceListener?.let { listener ->
            queue.peek()?.let { next ->
                listener.onNewNextPiece(next.type)
            }
        }
    }

    fun startGame() {
        timer.start()
    }

    fun pause() {
        timer.pause()
    }

    private fun generateNewPiece() {
        val type = pieceTypes[rng.nextInt(pieceTypes.size)]
        queue.add(Piece(type, rng.nextInt(columns / 2)))
    }

    private fun tick() {
        val piece = currentPiece ?: run {
            generateNewPiece()
            return
        }

        drop()
    }

    fun hold(): PieceType? {
        val oldHold = holdPiece
        holdPiece = currentPiece
        if (oldHold == null)
            pushNextPiece()
        else
            currentPiece = oldHold

        return holdPiece?.type
    }

    private fun drop() {
        val piece = currentPiece ?: return
        val colliders = piece.getDownColliders()
        for (coll in colliders) {
            if (coll.x >= rows || pile[coll.x][coll.y] != 0) {
                addToPile(piece)

                pushNextPiece()
                return
            }
        }
        piece.move(1, 0)
    }

    private fun pushNextPiece() {
        generateNewPiece()
        currentPiece = queue.poll()
        nextPieceListener?.let { listener ->
            queue.peek()?.let { next ->
                listener.onNewNextPiece(next.type)
            }
        }
    }

    private fun addToPile(piece: Piece) {
        Log.d("AddToPile", piece.coord.toString())
        if (piece.coord.x <= 0) {
            gameOverListener?.onGameOver()
            timer.pause()
            return
        }

        for (coord in piece.getBlockPositions())
            pile[coord.x][coord.y] = piece.color

        var diff = 0
        for (i in rows - 1 downTo 0) {
            var cnt = 0
            for (j in 0 until columns)
                if (pile[i][j] != 0)
                    cnt++
            if (cnt >= columns) {
                diff++
                lineClearListener?.onLineClear(lineClearScore)
            }

            if (i - diff >= 0)
                pile[i] = pile[i - diff]
            else
                pile[i] = IntArray(columns)
        }
    }

    fun rotate(dir: Int) {
        val p = currentPiece ?: return
        p.rotate(dir)
        // Check if the rotation is valid
        var dx = 0
        var dy = 0
        for (cr in p.getBlockPositions()) {
            dx = maxOf(dx, cr.x - rows + 1)
            dy = maxOf(dy, cr.y - columns + 1)
        }
        Log.d("Rotate move", "$dx,$dy")
        p.move(-dx, -dy)
        for (cr in p.getBlockPositions()) {
            Log.d("After rotate", "${cr.x},${cr.y}")
        }
    }

    fun steer(dir: Int) {
        val piece = currentPiece ?: return

        val colliders = if (dir < 0) piece.getLeftColliders() else piece.getRightColliders()
        for (coll in colliders) {
            if (coll.y >= columns || coll.y < 0 || pile[coll.x][coll.y] != 0)
                return
        }
        piece.move(0, dir)
    }

    fun hardDrop() {
        val piece = queue.peek()
        while (piece == queue.peek())
            drop()
    }

    fun getCurrentPiece(): Piece? = currentPiece

    fun getPile(): Array<IntArray> = pile

    fun setLineClearScore(score: Int) {
        lineClearScore = score
    }
}