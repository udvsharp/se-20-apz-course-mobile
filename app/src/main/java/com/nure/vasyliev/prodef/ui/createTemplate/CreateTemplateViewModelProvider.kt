package com.nure.vasyliev.prodef.ui.createTemplate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nure.vasyliev.prodef.rest.repositories.SharedPrefsRepository
import com.nure.vasyliev.prodef.rest.repositories.TemplateRepository

@Suppress("UNCHECKED_CAST")
class CreateTemplateViewModelFactory(
    private val sharedPrefsRepository: SharedPrefsRepository,
    private val templateRepository: TemplateRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateTemplateViewModel::class.java)) {
            return CreateTemplateViewModel(
                sharedPrefsRepository,
                templateRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}