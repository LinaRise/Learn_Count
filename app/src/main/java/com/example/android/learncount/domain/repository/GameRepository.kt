package com.example.android.learncount.domain.repository

import com.example.android.learncount.domain.entity.GameSettings
import com.example.android.learncount.domain.entity.Level
import com.example.android.learncount.domain.entity.Question

interface GameRepository {

    fun generateQuestion(
        maxSumValue: Int,
        countOfOptions: Int
    ): Question

    fun getGameSettings(level: Level): GameSettings
}