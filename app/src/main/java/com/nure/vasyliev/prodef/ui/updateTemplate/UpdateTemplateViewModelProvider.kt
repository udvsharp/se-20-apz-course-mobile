package com.nure.vasyliev.prodef.ui.updateTemplate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nure.vasyliev.prodef.rest.repositories.TemplateRepository

@Suppress("UNCHECKED_CAST")
class UpdateTemplateViewModelFactory(
    private val templateRepository: TemplateRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpdateTemplateViewModel::class.java)) {
            return UpdateTemplateViewModel(
                templateRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}