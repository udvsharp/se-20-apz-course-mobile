package com.nure.vasyliev.prodef.ui.updatePassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nure.vasyliev.prodef.rest.payloads.user.UpdatePasswordPayload
import com.nure.vasyliev.prodef.rest.repositories.SharedPrefsRepository
import com.nure.vasyliev.prodef.rest.repositories.UserRepository
import kotlinx.coroutines.launch

class UpdatePasswordViewModel(
    private val sharedPrefsRepository: SharedPrefsRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _iSuccess = MutableLiveData<Boolean>()
    val iSuccess: LiveData<Boolean> = _iSuccess

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun updatePassword(password: String) {
        val result = validatePassword(password)

        if (result) {
            viewModelScope.launch {
                try {
                    _isLoading.value = true
                    val userId = sharedPrefsRepository.getUserId() ?: ""
                    userRepository.updatePassword(
                        userId, UpdatePasswordPayload(
                            password = password
                        )
                    )
                    _isLoading.value = false
                    _iSuccess.value = true
                } catch (e: Exception) {
                    _isLoading.value = false
                    _iSuccess.value = false
                    e.printStackTrace()
                }
            }
        }
    }

    private fun validatePassword(password: String): Boolean {
        val result = password.length > 5
        if (!result) {
            _error.value = "Password error"
        }
        return result
    }
}
