package com.example.android.learncount.domain.entity

data class GameResult(
    val winner: Boolean,
    val countOfRightAnswers: Int,
    val countOfQuestions: GameSettings,
    val gameSettings: GameSettings
)