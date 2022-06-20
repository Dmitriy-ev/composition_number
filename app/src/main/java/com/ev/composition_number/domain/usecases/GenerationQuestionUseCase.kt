package com.ev.composition_number.domain.usecases

import com.ev.composition_number.domain.entity.Questions
import com.ev.composition_number.domain.repository.GameRepository

class GenerationQuestionUseCase(private val repository: GameRepository) {

    operator fun invoke(maxSumValue: Int): Questions {
        return repository.generationQuestions(maxSumValue, COUNT_OF_OPTIONS)
    }

    private companion object {
        const val COUNT_OF_OPTIONS = 6
    }
}