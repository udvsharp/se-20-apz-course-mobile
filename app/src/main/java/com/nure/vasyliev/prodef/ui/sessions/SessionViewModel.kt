package com.nure.vasyliev.prodef.ui.sessions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nure.vasyliev.prodef.model.pomodoro.Pomodoro
import com.nure.vasyliev.prodef.rest.repositories.PomodoroRepository
import com.nure.vasyliev.prodef.rest.repositories.SharedPrefsRepository
import kotlinx.coroutines.launch

class SessionViewModel(
    private val sharedPrefsRepository: SharedPrefsRepository,
    private val pomodoroRepository: PomodoroRepository
) : ViewModel() {

    private val _pomodoros = MutableLiveData<List<Pomodoro>>()
    val pomodoros: LiveData<List<Pomodoro>> = _pomodoros

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getAllPomodoros()
    }

    fun getAllPomodoros() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val userId = sharedPrefsRepository.getUserId()
                _pomodoros.value = pomodoroRepository.getAllUserPomodoro(userId ?: "").pomodoros
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                e.printStackTrace()
            }
        }
    }
}