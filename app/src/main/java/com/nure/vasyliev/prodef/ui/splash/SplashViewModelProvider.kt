package com.nure.vasyliev.prodef.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nure.vasyliev.prodef.rest.TokenInterceptor
import com.nure.vasyliev.prodef.rest.repositories.SharedPrefsRepository
import com.nure.vasyliev.prodef.rest.repositories.UserRepository

class SplashViewModelFactory(
    private val sharedPrefsRepository: SharedPrefsRepository,
    private val userRepository: UserRepository,
    private val tokenInterceptor: TokenInterceptor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(
                sharedPrefsRepository,
                userRepository,
                tokenInterceptor
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}