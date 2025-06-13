package com.dku.tetris

class HalController {
    companion object {
        init {
            System.loadLibrary("tetris")
        }
    }

    external fun ledWrite(value: Int)
    external fun segmentDisplay(number: Int)
    external fun oledWrite(text: String)
    external fun readDipSwitch(): Int
    external fun writeDotMatrix(data: ByteArray)
    external fun writeFullColorLed(data: ByteArray)
    external fun buzzerOn()
    external fun buzzerOff()
    external fun writeTextLcd(text: String)
}
