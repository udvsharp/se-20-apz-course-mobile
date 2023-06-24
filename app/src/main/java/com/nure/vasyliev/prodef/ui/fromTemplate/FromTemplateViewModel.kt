package com.nure.vasyliev.prodef.ui.fromTemplate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nure.vasyliev.prodef.model.template.Template
import com.nure.vasyliev.prodef.rest.repositories.PomodoroRepository
import com.nure.vasyliev.prodef.rest.repositories.SharedPrefsRepository
import com.nure.vasyliev.prodef.rest.repositories.TemplateRepository
import kotlinx.coroutines.launch

class FromTemplateViewModel(
    private val sharedPrefsRepository: SharedPrefsRepository,
    private val pomodoroRepository: PomodoroRepository,
    private val templateRepository: TemplateRepository
) : ViewModel() {

    private val _templates = MutableLiveData<List<Template>>()
    val templates: LiveData<List<Template>> = _templates

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getAllTemplates()
    }

    fun getAllTemplates() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val userId = sharedPrefsRepository.getUserId()
                _templates.value = templateRepository.getAllUserTemplates(userId ?: "").templates
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                e.printStackTrace()
            }
        }
    }

    fun createPomodoroFromTemplate(templateId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val userId = sharedPrefsRepository.getUserId()
                pomodoroRepository.createPomodoroFromTemplate(userId ?: "", templateId)
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                e.printStackTrace()
            }
        }
    }

    fun deleteTemplate(templateId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                templateRepository.deleteTemplate(templateId)
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                e.printStackTrace()
            }
        }
    }
}