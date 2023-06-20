package com.nure.vasyliev.prodef.rest.apis

import com.nure.vasyliev.prodef.model.user.User
import com.nure.vasyliev.prodef.rest.payloads.template.TemplatePayload
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TemplateApi {

    @GET("api/pomodoro/templates/:userId")
    suspend fun getUserTemplates(
        @Query("userId") userId: String
    ): Response<User>

    @POST("api/pomodoro/templates/:userId")
    suspend fun createUserTemplates(
        @Query("userId") userId: String,
        @Body payload: TemplatePayload
    ): Response<User>
}