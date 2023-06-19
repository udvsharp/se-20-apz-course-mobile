package com.nure.vasyliev.prodef.utils

sealed class TokenState {
    object WorkingJWT : TokenState()
    object ExpiredJWT : TokenState()
}