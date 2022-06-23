package com.ev.composition_number.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ev.composition_number.R
import com.ev.composition_number.data.GameRepositoryImpl
import com.ev.composition_number.domain.entity.GameResult
import com.ev.composition_number.domain.entity.GameSettings
import com.ev.composition_number.domain.entity.Level
import com.ev.composition_number.domain.entity.Questions
import com.ev.composition_number.domain.usecases.GenerationQuestionUseCase
import com.ev.composition_number.domain.usecases.GetGameSettingUseCase

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application

    private lateinit var gameSetting: GameSettings
    private lateinit var level: Level
    private val repository = GameRepositoryImpl

    private val generationQuestionsUseCase = GenerationQuestionUseCase(repository)
    private val getGameSettingUseCase = GetGameSettingUseCase(repository)

    private var timer: CountDownTimer? = null

    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    private val _question = MutableLiveData<Questions>()
    val question: LiveData<Questions>
        get() = _question

    private val _percentRightAnswers = MutableLiveData<Int>()
    val percentRightAnswers: LiveData<Int>
        get() = _percentRightAnswers

    private val _progressAnswers = MutableLiveData<String>()
    val progressAnswers: LiveData<String>
        get() = _progressAnswers

    private val _enoughCountRightAnswers = MutableLiveData<Boolean>()
    val enoughCountRightAnswers: LiveData<Boolean>
        get() = _enoughCountRightAnswers

    private val _enoughPercentRightAnswers = MutableLiveData<Boolean>()
    val enoughPercentRightAnswers: LiveData<Boolean>
        get() = _enoughPercentRightAnswers

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    private val _minPercent = MutableLiveData<Int>()
    val minPercent: LiveData<Int>
        get() = _minPercent

    private var countRightAnswers = 0
    private var countQuestions = 0

    fun startGame(level: Level) {
        getGameSettings(level)
        startTimer()
        generateQuestions()
        updateProgress()
    }

    fun chooseAnswer(answer: Int) {
        checkAnswer(answer)
        updateProgress()
        generateQuestions()
    }

    private fun updateProgress() {
        val percent = calculatePercentRightAnswers()
        _percentRightAnswers.value = percent
        _progressAnswers.value = String.format(
            context.resources.getString(R.string.progress_answers),
            countRightAnswers,
            gameSetting.minCountOfRightAnswers
        )
        _enoughCountRightAnswers.value = countRightAnswers >= gameSetting.minCountOfRightAnswers
        _enoughPercentRightAnswers.value = percent >= gameSetting.minPercentOfRightAnswers
    }

    private fun calculatePercentRightAnswers(): Int {
        if(countQuestions == 0) return 0
        return ((countRightAnswers / countQuestions.toDouble()) * 100).toInt()
    }

    private fun checkAnswer(answer: Int) {
        val rightAnswer = question.value?.rightAnswer
        if (answer == rightAnswer) {
            countRightAnswers++
        }
        countQuestions++
    }

    private fun getGameSettings(level: Level) {
        this.level = level
        this.gameSetting = getGameSettingUseCase(level)
        _minPercent.value = gameSetting.minPercentOfRightAnswers
    }

    private fun startTimer() {
        timer = object : CountDownTimer(
            gameSetting.gameTime * MILLIS_IN_SECONDS, MILLIS_IN_SECONDS
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

    private fun generateQuestions() {
        _question.value = generationQuestionsUseCase(gameSetting.maxSum)
    }

    private fun finishGame() {
        _gameResult.value = GameResult(
            winner = enoughCountRightAnswers.value == true && enoughPercentRightAnswers.value == true,
            countOfRightAnswers = countRightAnswers,
            countOfQuestions = countQuestions,
            gameSettings = gameSetting
        )
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    private fun formatTime(mill: Long): String {
        val seconds = mill / MILLIS_IN_SECONDS
        val minutes = seconds / SECONDS_IN_MINUTES
        val leftSeconds = seconds - (minutes * SECONDS_IN_MINUTES)
        return String.format("%02d:%02d", minutes, leftSeconds)
    }

    companion object {
        private const val MILLIS_IN_SECONDS = 1000L
        private const val SECONDS_IN_MINUTES = 60
    }
}