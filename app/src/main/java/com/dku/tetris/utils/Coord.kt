package com.dku.tetris.utils

data class Coord(var x: Int, var y: Int) {

    fun translate(dx: Int, dy: Int) {
        x += dx
        y += dy
    }

    override fun toString(): String {
        return "$x,$y"
    }
}
