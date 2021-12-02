package com.example.android.learncount.domain.usecases

import com.example.android.learncount.domain.entity.GameSettings
import com.example.android.learncount.domain.entity.Question
import com.example.android.learncount.domain.repository.GameRepository

class GenerateQuestionsUseCase(private val repository: GameRepository) {

    //if invoke operator used then you can call method by parameter declare generateQuestionsUseCase
    operator fun invoke(masSumValue: Int): Question {
        return repository.generateQuestion(masSumValue, COUNT_OF_OPTIONS)
    }

    private companion object {
        private const val COUNT_OF_OPTIONS = 6
    }
}