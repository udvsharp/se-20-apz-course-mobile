package com.nure.vasyliev.prodef.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nure.vasyliev.prodef.rest.TokenInterceptor
import com.nure.vasyliev.prodef.rest.repositories.SharedPrefsRepository
import com.nure.vasyliev.prodef.rest.repositories.UserRepository
import com.nure.vasyliev.prodef.utils.TokenState

class SplashViewModel(
    private val sharedPrefsRepository: SharedPrefsRepository,
    private val userRepository: UserRepository,
    private val tokenInterceptor: TokenInterceptor
) : ViewModel() {

    private val _tokenState = MutableLiveData<TokenState>()
    val tokenState: LiveData<TokenState> = _tokenState

    init {
    }
}