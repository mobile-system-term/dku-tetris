package com.dku.tetris

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import com.dku.tetris.gameplay.board.Board
import com.dku.tetris.gameplay.board.BoardView
import com.dku.tetris.gameplay.piece.PieceView
import com.dku.tetris.gameplay.piece.PieceType

import com.dku.tetris.hw.HwModule
import com.dku.tetris.hw.Piezo
import com.dku.tetris.hw.agent.SegmentAgent

import com.dku.tetris.settings.dao.SettingDAO
import com.dku.tetris.settings.dto.SettingDTO


class GameActivity : AppCompatActivity(), Board.OnLineClearListener, Board.OnGameOverListener {

    private lateinit var boardView: BoardView
    private lateinit var scoreView: TextView
    private var score = 0
    private var combo = 0
    private var isGameOver = false

    private val segment = SegmentAgent()
    private val piezo: Piezo = HwModule.piezo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        piezo.out(30, 100)
        piezo.out(0, 100)

        segment.print(0)

        val setting: SettingDTO? = SettingDAO.getSetting(applicationContext)
        boardView = findViewById(R.id.boardView)

        val holdView: PieceView = findViewById(R.id.holdPieceView)
        holdView.setOnClickListener {
            boardView.hold()?.let { it1 -> holdView.setPiece(it1) }
        }

        val nextPieceView: PieceView = findViewById(R.id.nextPieceView)
        boardView.setOnNextPieceListener(object : Board.OnNextPieceListener {
            override fun onNewNextPiece(nextPiece: PieceType) {
                nextPieceView.setPiece(nextPiece)
            }
        })

        boardView.setOnLineClearListener(this)
        boardView.setOnGameOverListener(this)

        boardView.setLineClearScore(setting!!.lineScore.toInt())

        scoreView = findViewById(R.id.scoreTextView)
        scoreView.text = "0"
    }

    override fun onPause() {
        segment.stopThread()
        boardView.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        isGameOver = false
    }

    override fun onLineClear(score: Int) {
        combo += 1
        this.score += score + combo * 100
        scoreView.text = this.score.toString()

        segment.print(this.score)
    }

    override fun onGameOver() {
        isGameOver = true
        segment.stop()
        finish()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        Log.d("BoardView", "KeyEvent: ${event.keyCode}, Action: ${event.action}")
        print("KeyEvent: ${event.keyCode}, Action: ${event.action}")
        if (event.action == KeyEvent.ACTION_DOWN) {
            when (event.keyCode) {
                11 -> boardView.steerLeft() // move left
                12 -> boardView.steerRight()  // move right
                13 -> boardView.rotate() // rotate piece
                else -> return super.onKeyDown(keyCode, event);
            }
            return true
        }
        return super.onKeyDown(keyCode, event);
    }
}