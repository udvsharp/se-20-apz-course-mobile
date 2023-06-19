package com.nure.vasyliev.prodef.rest.apis

import com.nure.vasyliev.prodef.model.user.AuthResponse
import com.nure.vasyliev.prodef.rest.payloads.LoginPayload
import com.nure.vasyliev.prodef.rest.payloads.RegisterPayload
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/auth/login")
    suspend fun login(@Body payload: LoginPayload): Response<AuthResponse>

    @POST("api/auth/register")
    suspend fun register(@Body payload: RegisterPayload): Response<AuthResponse>
}
