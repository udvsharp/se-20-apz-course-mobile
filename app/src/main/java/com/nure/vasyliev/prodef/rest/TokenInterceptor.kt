package com.nure.vasyliev.prodef.rest

import okhttp3.Interceptor
import okhttp3.Response

object TokenInterceptor : Interceptor {

    private var token: String = ""

    fun updateToken(newToken: String) {
        token = newToken
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val modifiedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
        return chain.proceed(modifiedRequest)
    }
}