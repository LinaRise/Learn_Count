package com.example.android.learncount.presentation.fragments

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.learncount.R
import com.example.android.learncount.data.GameRepositoryImpl
import com.example.android.learncount.domain.entity.GameResult
import com.example.android.learncount.domain.entity.GameSettings
import com.example.android.learncount.domain.entity.Level
import com.example.android.learncount.domain.entity.Question
import com.example.android.learncount.domain.usecases.GenerateQuestionsUseCase
import com.example.android.learncount.domain.usecases.GetGameSettingsUseCase

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GameRepositoryImpl
    private lateinit var gameSettings: GameSettings
    private lateinit var level: Level
    private var timer: CountDownTimer? = null
    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private val _percentOfRightAnswers = MutableLiveData<Int>()
    val percentOfRightAnswers: LiveData<Int>
        get() = _percentOfRightAnswers

    private val _progressAnswers = MutableLiveData<String>()
    val progressAnswers: LiveData<String>
        get() = _progressAnswers

    private val _enoughCount = MutableLiveData<Boolean>()
    val enoughCount: LiveData<Boolean>
        get() = _enoughCount

    private val _enoughPercent = MutableLiveData<Boolean>()
    val enoughPercent: LiveData<Boolean>
        get() = _enoughPercent

    private val _minPercent = MutableLiveData<Int>()
    val minPercent: LiveData<Int>
        get() = _minPercent

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    private var countOfRightAnswers = 0
    private var countOfQuestions = 0


    private val generateQuestionUseCase = GenerateQuestionsUseCase(repository)
    private val getGamsSettingsUseCase = GetGameSettingsUseCase(repository)

    fun startGame(level: Level) {
        getGameSettings(level)
        startTimer()
        generateQuestion()
        updateProgress()
    }

    private fun updateProgress() {
        val percent = calculatePercentOfRightAnswers()
        _percentOfRightAnswers.value = percent
        _progressAnswers.value = String.format(
            getApplication<Application>().applicationContext.resources.getString(R.string.progress_answers),
            countOfRightAnswers,
            gameSettings.minCountRightAnswers
        )
        _enoughCount.value = countOfRightAnswers >= gameSettings.minCountRightAnswers
        _enoughPercent.value = percent >= gameSettings.minPercentageOfRightAnswers
    }

    private fun calculatePercentOfRightAnswers(): Int {
        if (countOfQuestions == 0)
            return 0
        return ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
    }

    private fun getGameSettings(level: Level) {
        this.level = level
        this.gameSettings = getGamsSettingsUseCase(level)
        _minPercent.value = gameSettings.minPercentageOfRightAnswers

    }

    private fun startTimer() {
        timer = object : CountDownTimer(
            (gameSettings.gameTimeInSeconds * MILLIS_IN_SECONDS).toLong(),
            MILLIS_IN_SECONDS.toLong()
        ) {
            override fun onTick(p0: Long) {
                _formattedTime.value = formatTime(p0)
            }

            override fun onFinish() {
                finishGame()
            }

        }
        timer?.start()
    }

    private fun generateQuestion() {
        _question.value = generateQuestionUseCase(gameSettings.maxSumValue)
    }

    fun chooseAnswer(number: Int) {
        checkAnswer(number)
        updateProgress()
        generateQuestion()
    }

    private fun checkAnswer(number: Int) {
        val rightAnswer = _question.value?.rightAnswer
        if (number == rightAnswer) {
            countOfRightAnswers++
        }
        countOfQuestions++
    }

    private fun formatTime(millisUntilFinish: Long): String {
        val seconds = millisUntilFinish / MILLIS_IN_SECONDS
        val minutes = seconds / SECONDS_IN_MINUTES
        val leftSeconds = seconds - minutes * SECONDS_IN_MINUTES
        return String.format("%02d:%02d", minutes, leftSeconds)
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    private fun finishGame() {
        _gameResult.value = GameResult(
            enoughCount.value == true && enoughPercent.value == true,
            countOfRightAnswers,
            countOfQuestions,
            gameSettings
        )
    }

    companion object {
        private const val MILLIS_IN_SECONDS = 1000
        private const val SECONDS_IN_MINUTES = 60
    }


}