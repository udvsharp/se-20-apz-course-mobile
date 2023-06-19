package com.nure.vasyliev.prodef.ui.signUp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nure.vasyliev.prodef.rest.TokenInterceptor
import com.nure.vasyliev.prodef.rest.repositories.AuthRepository
import com.nure.vasyliev.prodef.rest.repositories.SharedPrefsRepository
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val sharedPrefsRepository: SharedPrefsRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val tokenInterceptor = TokenInterceptor

    private val _token = MutableLiveData<String>()
    val token: LiveData<String> = _token

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun register(
        name: String,
        username: String,
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val pair = authRepository.signUp(name, username, email, password)

                if (pair.first == null && pair.second == null) {
                    throw Exception("Wrong email or password")
                }

                pair.first?.let { user ->
                    sharedPrefsRepository.putUserId(user.id)
                }

                pair.second?.let { token ->
                    tokenInterceptor.updateToken(token)
                    sharedPrefsRepository.putUserToken(token)
                    _token.value = token
                }

                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                _error.value = "Wrong email or password"
            }
        }
    }
}