package com.dku.tetris.hw

import android.os.Vibrator

object HwContainer {
    val segment = Segment()
    val piezo = Piezo()
    var vibrator: Vibrator? = null
}