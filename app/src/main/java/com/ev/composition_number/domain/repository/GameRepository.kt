package com.ev.composition_number.domain.repository

import com.ev.composition_number.domain.entity.GameSettings
import com.ev.composition_number.domain.entity.Level
import com.ev.composition_number.domain.entity.Questions

interface GameRepository {

    fun getGameSettings(level: Level): GameSettings

    fun generationQuestions(maxSumValue: Int, countOfOptions: Int): Questions

}