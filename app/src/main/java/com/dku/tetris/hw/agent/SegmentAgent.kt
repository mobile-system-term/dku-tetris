package com.dku.tetris.hw.agent

import com.dku.tetris.hw.Segment

class SegmentAgent {
    private val segment = Segment()

    fun print(score: Int) {
        segment.print(score)
    }

    fun stop() {
        segment.stop()
    }

    fun stopThread() {
        segment.stopThread()
    }
}
