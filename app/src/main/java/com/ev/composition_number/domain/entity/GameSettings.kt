package com.ev.composition_number.domain.entity

data class GameSettings(
    val maxSum: Int,
    val minCountOfRightAnswers: Int,
    val minPercentOfRightAnswers: Int,
    val gameTime: Int
)
