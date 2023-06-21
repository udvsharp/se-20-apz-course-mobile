package com.nure.vasyliev.prodef.ui.sessions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nure.vasyliev.prodef.rest.repositories.PomodoroRepository
import com.nure.vasyliev.prodef.rest.repositories.SharedPrefsRepository

@Suppress("UNCHECKED_CAST")
class SessionViewModelFactory(
    private val sharedPrefsRepository: SharedPrefsRepository,
    private val pomodoroRepository: PomodoroRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SessionViewModel::class.java)) {
            return SessionViewModel(
                sharedPrefsRepository,
                pomodoroRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}