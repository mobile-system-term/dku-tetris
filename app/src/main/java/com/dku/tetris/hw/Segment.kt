package com.dku.tetris.hw


import android.os.SystemClock
import com.dku.tetris.HwController.segmentControl
import kotlin.concurrent.thread

class Segment {
    private val thresholdMax = 1_000_000
    private val thresholdMin = -1

    private var thread: Thread = thread(start = true) {
        while (true) {
            segmentControl(0)
            SystemClock.sleep(0)
        }
    }
    private var printData: Int = 0

    fun print(i: Int) {
        printData = when {
            i in (thresholdMin + 1)..<thresholdMax -> i
            i <= thresholdMin -> thresholdMin + 1
            else -> thresholdMax - 1
        }
    }

    fun stop() {
        printData = 0
    }

    fun stopThread() {
        thread.interrupt()
    }
}