package com.dku.tetris.settings.dto

class SettingDTO(
    val lineScore: Long,
    val timeLevels: MutableList<MutableList<Int>>?,
    val width: Int,
    val height: Int
)