package com.dku.tetris

object HwController {

    external fun ledControl(bitCount: Int): String

    external fun segmentControl(data: Int): String

    external fun lcdClear(): String

    external fun lcdPrint(lineIndex: Int, msg: String): String

    external fun dotMatrixControl(data: String): String

    external fun piezoControl(i: Char): String

    external fun fullColorLedControl(ledNum: Int, red: Int, green: Int, blue: Int): String
}