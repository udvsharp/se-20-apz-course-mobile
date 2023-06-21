package com.nure.vasyliev.prodef.ui.createPomodoro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nure.vasyliev.prodef.rest.repositories.PomodoroRepository
import com.nure.vasyliev.prodef.rest.repositories.SharedPrefsRepository

@Suppress("UNCHECKED_CAST")
class CreateSessionViewModelFactory(
    private val sharedPrefsRepository: SharedPrefsRepository,
    private val pomodoroRepository: PomodoroRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateSessionViewModel::class.java)) {
            return CreateSessionViewModel(
                sharedPrefsRepository,
                pomodoroRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}