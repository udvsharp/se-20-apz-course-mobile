package com.nure.vasyliev.prodef.ui.pomodoro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nure.vasyliev.prodef.service.PomodoroSharedPrefs
import com.nure.vasyliev.prodef.utils.MILLIS_IN_SECOND

class PomodoroViewModel(
    private val pomodoroSharedPrefs: PomodoroSharedPrefs
) : ViewModel() {

    private var _millis = MutableLiveData(0L)
    var millis: LiveData<Long> = _millis

    private var _maxSeconds = MutableLiveData(0L)
    var maxSeconds: LiveData<Long> = _maxSeconds

    private var _isStarted = MutableLiveData(false)
    var isStarted: LiveData<Boolean> = _isStarted

    init {
        _maxSeconds.value = pomodoroSharedPrefs.getMaxMillis() / MILLIS_IN_SECOND
        _millis.value = pomodoroSharedPrefs.getMillis() / MILLIS_IN_SECOND
    }

    fun updateMillis(newMillis: Long) {
        _millis.value = newMillis
    }

    fun updateStarted(newStarted: Boolean) {
        _isStarted.value = newStarted
    }

    fun updateMaxSeconds(seconds: Long) {
        _maxSeconds.value = seconds
    }
}