package com.nure.vasyliev.prodef.rest.apis

import com.nure.vasyliev.prodef.model.pomodoro.FilteredPomodoros
import com.nure.vasyliev.prodef.model.pomodoro.Pomodoro
import com.nure.vasyliev.prodef.model.pomodoro.Pomodoros
import com.nure.vasyliev.prodef.rest.payloads.pomodoro.PomodoroPayload
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PomodoroApi {

    @GET("api/pomodoro/{userId}")
    suspend fun getAllUserPomodoro(
        @Path("userId") userId: String
    ): Response<Pomodoros>

    @GET("api/pomodoro/{userId}/valid")
    suspend fun getAllValidUserPomodoro(
        @Path("userId") userId: String
    ): Response<FilteredPomodoros>

    @POST("api/pomodoro/{userId}")
    suspend fun createPomodoro(
        @Path("userId") userId: String,
        @Body payload: PomodoroPayload
    ): Response<Pomodoro>

    @POST("api/pomodoro/{userId}/template/{templateId}")
    suspend fun createPomodoroFromTemplate(
        @Path("userId") userId: String,
        @Path("templateId") templateId: String
    ): Response<Pomodoro>

    @DELETE("api/pomodoro/{pomodoroId}")
    suspend fun deletePomodoro(
        @Path("pomodoroId") pomodoroId: String
    ) : Response<String>

    @POST("api/pomodoro/{userId}/start")
    suspend fun startPomodoro(
        @Path("userId") userId: String
    ): Response<Pomodoro>

    @POST("api/pomodoro/{userId}/stop")
    suspend fun stopPomodoro(
        @Path("userId") userId: String
    ): Response<Pomodoro>
}