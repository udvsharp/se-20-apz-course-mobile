package com.nure.vasyliev.prodef.ui.createPomodoro

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nure.vasyliev.prodef.rest.repositories.PomodoroRepository
import com.nure.vasyliev.prodef.rest.repositories.SharedPrefsRepository
import kotlinx.coroutines.launch

class CreateSessionViewModel(
    private val sharedPrefsRepository: SharedPrefsRepository,
    private val pomodoroRepository: PomodoroRepository
) : ViewModel() {

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _taskNameError = MutableLiveData<String>()
    val taskNameError: LiveData<String> = _taskNameError

    private val _durationError = MutableLiveData<String>()
    val durationError: LiveData<String> = _durationError

    fun createPomodoro(taskName: String, durationMins: String) {
        val duration = if (durationMins.isNotBlank() && durationMins.isDigitsOnly()) {
            durationMins.toInt()
        } else {
            0
        }

        val taskNameValidationResult = validateTaskName(taskName)
        val durationValidationResult = validateDuration(duration)

        if (taskNameValidationResult && durationValidationResult) {
            viewModelScope.launch {
                try {
                    _isLoading.value = true
                    val userId = sharedPrefsRepository.getUserId()
                    pomodoroRepository.createPomodoro(userId ?: "", taskName, duration)
                    _isSuccess.value = true
                    _isLoading.value = false
                } catch (e: Exception) {
                    _isLoading.value = false
                    e.printStackTrace()
                }
            }
        }
    }

    private fun validateTaskName(taskName: String): Boolean {
        if (taskName.isBlank()) {
            _taskNameError.value = "Task name is empty"
        }
        return taskName.isNotBlank()
    }

    private fun validateDuration(durationMins: Int): Boolean {
        if (durationMins < 5) {
            _durationError.value = "Task name is empty"
        }
        return durationMins in 5..480
    }
}