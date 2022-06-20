package com.ev.composition_number.domain.usecases

import com.ev.composition_number.domain.entity.GameSettings
import com.ev.composition_number.domain.entity.Level
import com.ev.composition_number.domain.repository.GameRepository

class GetGameSettingUseCase(private val repository: GameRepository) {

    operator fun invoke(level: Level): GameSettings {
        return repository.getGameSettings(level)
    }
}