package com.example.android.learncount.domain.entity

data class GameSettings(
    val maxSumValue:Int,
    val minCountRightAnswers:Int,
    val minPercentageOfRightAnswers:Int,
    val gameTimeInSeconds:Int
)
