package com.nure.vasyliev.prodef.ui.fromTemplate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nure.vasyliev.prodef.rest.repositories.PomodoroRepository
import com.nure.vasyliev.prodef.rest.repositories.SharedPrefsRepository
import com.nure.vasyliev.prodef.rest.repositories.TemplateRepository

@Suppress("UNCHECKED_CAST")
class FromTemplateViewModelProvider(
    private val sharedPrefsRepository: SharedPrefsRepository,
    private val templateRepository: TemplateRepository,
    private val pomodoroRepository: PomodoroRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FromTemplateViewModel::class.java)) {
            return FromTemplateViewModel(
                sharedPrefsRepository,
                pomodoroRepository,
                templateRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}