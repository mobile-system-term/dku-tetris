package com.dku.tetris.gameplay.board

import android.os.Handler;
import android.util.Log;
import android.util.Pair;

import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

class BoardTimer(
    private val handler: Handler,
    private val levels: Queue<Pair<Int, Int>>
) {
    companion object {
        const val MAX_INTERVAL = 1200
        const val MIN_INTERVAL = 200
    }

    private var timer: Timer? = null
    private var elapsedTime = 0
    private var dropInterval = 0

    init {
        if (levels.isEmpty())
            changeSpeed(MAX_INTERVAL)
        else
            changeSpeed(levels.peek().second)
    }

    fun start() {
        pause()
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                handler.sendMessage(handler.obtainMessage())
                elapsedTime += dropInterval
                if (levels.isNotEmpty()) {
                    val top = levels.peek()
                    if (elapsedTime >= top.first * 1000) {
                        val level = levels.remove()
                        changeSpeed(level.second)
                    }
                }
            }
        }, 0, dropInterval.toLong())
    }

    fun pause() {
        try {
            timer?.cancel()
        } catch (e: NullPointerException) {
            Log.e("BoardTimer", "Hasn't been started")
        }
    }

    fun changeSpeed(speed: Int) {
        dropInterval = MAX_INTERVAL.coerceAtMost((MAX_INTERVAL - speed).coerceAtLeast(MIN_INTERVAL))
        Log.d("BoardTimer", "Speed: $dropInterval")
        pause()
        start()
    }
}
