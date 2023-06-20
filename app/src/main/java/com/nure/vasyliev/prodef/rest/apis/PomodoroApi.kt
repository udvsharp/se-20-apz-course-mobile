package com.nure.vasyliev.prodef.rest.apis

import com.nure.vasyliev.prodef.model.pomodoro.Pomodoros
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PomodoroApi {

    @GET("api/pomodoro/:userId")
    suspend fun getAllUserPomodoro(
        @Query("userId") userId: String
    ): Response<Pomodoros>
}