package com.dku.tetris.hw

import com.dku.tetris.HwController.piezoControl
import kotlin.concurrent.thread

class Piezo {

    private val sounds: MutableList<Int> = mutableListOf()
    private val beats: MutableList<Int> = mutableListOf()

    init {
        sounds.clear()
        beats.clear()

        thread(start = true) {
            while (true) {
                try {
                    if (sounds.isEmpty() || beats.isEmpty()) {
                        piezoControl(0.toChar())
                        continue
                    }

                    val sound = sounds[0]
                    val beat = beats[0]
                    piezoControl(sound.toChar())
                    Thread.sleep(beat.toLong())
                    sounds.removeAt(0)
                    beats.removeAt(0)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun out(sound: Int, beat: Int) {
        sounds.add(sound)
        beats.add(beat)
    }
}