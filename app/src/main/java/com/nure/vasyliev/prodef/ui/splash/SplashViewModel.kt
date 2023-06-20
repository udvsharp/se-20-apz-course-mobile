package com.nure.vasyliev.prodef.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nure.vasyliev.prodef.rest.TokenInterceptor
import com.nure.vasyliev.prodef.rest.repositories.PomodoroRepository
import com.nure.vasyliev.prodef.rest.repositories.SharedPrefsRepository
import com.nure.vasyliev.prodef.utils.TokenState
import kotlinx.coroutines.launch

class SplashViewModel(
    private val sharedPrefsRepository: SharedPrefsRepository,
    private val pomodoroRepository: PomodoroRepository,
    private val tokenInterceptor: TokenInterceptor
) : ViewModel() {

    private val _tokenState = MutableLiveData<TokenState>()
    val tokenState: LiveData<TokenState> = _tokenState

    init {
        viewModelScope.launch {
            try {
                val token = sharedPrefsRepository.getUserToken()
                val userId = sharedPrefsRepository.getUserId()
                token?.let {
                    tokenInterceptor.updateToken(token)
                }
                pomodoroRepository.getAllUserPomodoro(userId ?: "")
                _tokenState.value = TokenState.WorkingJWT
            } catch (e: Exception) {
                _tokenState.value = TokenState.ExpiredJWT
            }
        }
    }
}