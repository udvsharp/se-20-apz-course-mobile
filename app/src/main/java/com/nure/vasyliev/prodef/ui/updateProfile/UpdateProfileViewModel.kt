package com.nure.vasyliev.prodef.ui.updateProfile

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nure.vasyliev.prodef.model.user.User
import com.nure.vasyliev.prodef.rest.payloads.user.UpdateUserPayload
import com.nure.vasyliev.prodef.rest.repositories.SharedPrefsRepository
import com.nure.vasyliev.prodef.rest.repositories.UserRepository
import kotlinx.coroutines.launch

class UpdateProfileViewModel(
    private val sharedPrefsRepository: SharedPrefsRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _iSuccess = MutableLiveData<Boolean>()
    val iSuccess: LiveData<Boolean> = _iSuccess

    private val _error = MutableLiveData<UpdateUserError>()
    val error: LiveData<UpdateUserError> = _error

    init {
        getUser()
    }

    fun getUser() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val userId = sharedPrefsRepository.getUserId()
                val pomodoro = userRepository.getUser(userId ?: "")
                _user.value = pomodoro
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                e.printStackTrace()
            }
        }
    }

    fun updateUser(name: String, username: String, email: String) {
        val result1 = validateName(name)
        val result2 = validateUsername(username)
        val result3 = validateEmail(email)

        if (result1 && result2 && result3) {
            user.value?.let {
                val newName = if (it.name == name) null else name
                val newUsername = if (it.username == username) null else username
                val newEmail = if (it.email == email) null else email
                viewModelScope.launch {
                    try {
                        _isLoading.value = true
                        val userId = sharedPrefsRepository.getUserId() ?: ""
                        userRepository.updateUser(
                            userId, UpdateUserPayload(
                                name = newName,
                                username = newUsername,
                                email = newEmail,
                                roles = listOf("user")
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
    }

    private fun validateName(name: String): Boolean {
        val result = name.isEmpty()
        if (result) {
            _error.value = UpdateUserError.NameError
        }
        return !result
    }

    private fun validateUsername(username: String): Boolean {
        val result = username.isEmpty()
        if (result) {
            _error.value = UpdateUserError.UsernameError
        }
        return !result
    }

    private fun validateEmail(email: String): Boolean {
        val result = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        if (!result) {
            _error.value = UpdateUserError.EmailError
        }
        return result
    }
}

sealed class UpdateUserError {
    object NameError : UpdateUserError()
    object UsernameError : UpdateUserError()
    object EmailError : UpdateUserError()
}