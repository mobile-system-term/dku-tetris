package com.dku.tetris.gameplay.piece

import com.dku.tetris.R

enum class PieceType(
    b1: Int,
    b2: Int,
    b3: Int,
    b4: Int,
    val color: Int
) {
    SQUARE(0, 1, 4, 5, R.color.black),
    L(2, 4, 5, 6, R.color.black),
    LR(0, 1, 2, 6, R.color.black),
    STRAIGHT(0, 1, 2, 3, R.color.black),
    Z(0, 1, 5, 6, R.color.black),
    ZR(1, 2, 4, 5, R.color.black),
    T(1, 4, 5, 6, R.color.black);

    private val data: IntArray = intArrayOf(b1, b2, b3, b4)
    val height: Int = if (b4 > 3) 2 else 1
    val width: Int = b4 % 4 + 1

    fun getData(): IntArray = data
}
