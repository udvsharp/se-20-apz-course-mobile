package com.nure.vasyliev.prodef.rest.apis

import com.nure.vasyliev.prodef.model.pomodoro.Pomodoro
import com.nure.vasyliev.prodef.model.pomodoro.Pomodoros
import com.nure.vasyliev.prodef.rest.payloads.pomodoro.PomodoroPayload
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PomodoroApi {

    @GET("api/pomodoro/{userId}")
    suspend fun getAllUserPomodoro(
        @Path("userId") userId: String
    ): Response<Pomodoros>

    @POST("api/pomodoro/{userId}")
    suspend fun createPomodoro(
        @Path("userId") userId: String,
        @Body payload: PomodoroPayload
    ): Response<Pomodoro>
}