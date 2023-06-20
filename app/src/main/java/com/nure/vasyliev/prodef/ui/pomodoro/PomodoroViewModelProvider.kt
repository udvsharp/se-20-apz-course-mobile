package com.nure.vasyliev.prodef.ui.pomodoro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nure.vasyliev.prodef.service.PomodoroSharedPrefs

class PomodoroViewModelFactory(
    private val pomodoroSharedPrefs: PomodoroSharedPrefs
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PomodoroViewModel::class.java)) {
            return PomodoroViewModel(
                pomodoroSharedPrefs
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}