package com.dku.tetris.gameplay.piece

import com.dku.tetris.utils.Coord


class Piece(val type: PieceType, columns: Int) {
    val color: Int = type.color
    private var rotation: Int = 0
    private val coord = Coord(0, columns)

    var blockPos: Array<Coord> = arrayOf()
        private set
    var downColliders: List<Coord> = listOf()
        private set
    var leftColliders: List<Coord> = listOf()
        private set
    var rightColliders: List<Coord> = listOf()
        private set

    init {
        updateBlocks()
    }

    fun rotate(i: Int) {
        rotation = (rotation + i) % 4
        if (rotation < 0) rotation += 4
        updateBlocks()
    }

    fun move(dx: Int, dy: Int) {
        coord.translate(dx, dy)
        updateBlocks()
    }

    private fun updateBlocks() {
        val set = mutableSetOf<Coord>()
        val data = type.getData()
        blockPos = Array(4) { Coord(0, 0) }

        for (i in 0..3) {
            var j = data[i] % 4
            if (rotation > 1) j = 4 - j
            val k = if ((rotation == 0 || rotation == 3) == (data[i] < 4)) 0 else 1

            blockPos[i] = if (rotation % 2 == 0)
                Coord(coord.x + k, coord.y + j)
            else
                Coord(coord.x + j, coord.y + k)

            if (rotation == 2) blockPos[i].translate(0, -1)
            if (rotation == 3) blockPos[i].translate(-1, 0)

            set.add(blockPos[i])
        }

        rightColliders = mutableListOf()
        leftColliders = mutableListOf()
        downColliders = mutableListOf()

        for (c in blockPos) {
            updateCollider(set, downColliders as MutableList, c.x + 1, c.y)
            updateCollider(set, rightColliders as MutableList, c.x, c.y + 1)
            updateCollider(set, leftColliders as MutableList, c.x, c.y - 1)
        }
    }

    private fun updateCollider(set: Set<Coord>, colliders: MutableList<Coord>, x: Int, y: Int) {
        val c = Coord(x, y)
        if (!set.contains(c)) {
            colliders.add(c)
        }
    }

    fun getBlockPositions(): Array<Coord> = blockPos

    fun getDownColliders(): Array<Coord> = downColliders.toTypedArray()

    fun getLeftColliders(): Array<Coord> = leftColliders.toTypedArray()

    fun getRightColliders(): Array<Coord> = rightColliders.toTypedArray()

    fun getCoord(): Coord = coord

    override fun toString(): String {
        return "${type.name}_PIECE"
    }
}
