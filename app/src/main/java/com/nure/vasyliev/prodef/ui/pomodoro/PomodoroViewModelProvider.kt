package com.nure.vasyliev.prodef.ui.pomodoro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nure.vasyliev.prodef.rest.repositories.PomodoroRepository
import com.nure.vasyliev.prodef.rest.repositories.SharedPrefsRepository
import com.nure.vasyliev.prodef.service.PomodoroSharedPrefs

@Suppress("UNCHECKED_CAST")
class PomodoroViewModelFactory(
    private val sharedPrefsRepository: SharedPrefsRepository,
    private val pomodoroRepository: PomodoroRepository,
    private val pomodoroSharedPrefs: PomodoroSharedPrefs
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PomodoroViewModel::class.java)) {
            return PomodoroViewModel(
                sharedPrefsRepository,
                pomodoroRepository,
                pomodoroSharedPrefs
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}