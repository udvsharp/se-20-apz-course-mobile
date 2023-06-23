package com.nure.vasyliev.prodef.ui.pomodoro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nure.vasyliev.prodef.model.pomodoro.Pomodoro
import com.nure.vasyliev.prodef.rest.repositories.PomodoroRepository
import com.nure.vasyliev.prodef.rest.repositories.SharedPrefsRepository
import com.nure.vasyliev.prodef.service.PomodoroSharedPrefs
import com.nure.vasyliev.prodef.utils.SECONDS_IN_MINUTE
import kotlinx.coroutines.launch

class PomodoroViewModel(
    private val sharedPrefsRepository: SharedPrefsRepository,
    private val pomodoroRepository: PomodoroRepository,
    private val pomodoroSharedPrefs: PomodoroSharedPrefs
) : ViewModel() {

    private var _validPomodoro = MutableLiveData<Pomodoro?>()
    var validPomodoro: LiveData<Pomodoro?> = _validPomodoro

    private var _millis = MutableLiveData<Long>()
    var millis: LiveData<Long> = _millis

    private var _maxSeconds = MutableLiveData<Long>()
    var maxSeconds: LiveData<Long> = _maxSeconds

    private var _isStarted = MutableLiveData<Boolean>()
    var isStarted: LiveData<Boolean> = _isStarted

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        _isStarted.value = pomodoroSharedPrefs.getIsStarted()
        getLastValidPomodoro()
    }

    fun getLastValidPomodoro() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val userId = sharedPrefsRepository.getUserId()
                val pomodoro = pomodoroRepository.getLastValidUserPomodoro(userId ?: "")
                _validPomodoro.value = pomodoro
                _maxSeconds.value = (pomodoro?.durationMins ?: 0) * SECONDS_IN_MINUTE
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                e.printStackTrace()
            }
        }
    }

    fun updateMillis(newMillis: Long) {
        _millis.value = newMillis
    }

    fun updateStarted(newStarted: Boolean) {
        _isStarted.value = newStarted
    }
}