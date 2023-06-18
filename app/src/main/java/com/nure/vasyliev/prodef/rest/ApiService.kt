package com.nure.vasyliev.prodef.rest

import com.nure.vasyliev.prodef.rest.payload.LoginPayload
import com.nure.vasyliev.prodef.rest.payload.RegisterPayload
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET

interface ApiService {
    @GET("/api/auth/login")
    suspend fun login(@Body payload: LoginPayload): Response<ResponseBody>

    @GET("/api/auth/register")
    suspend fun register(@Body payload: RegisterPayload): Response<ResponseBody>
}
