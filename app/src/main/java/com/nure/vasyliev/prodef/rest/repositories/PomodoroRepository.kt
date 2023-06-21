package com.nure.vasyliev.prodef.rest.repositories

import com.nure.vasyliev.prodef.model.pomodoro.Pomodoros
import com.nure.vasyliev.prodef.rest.ApiClient
import com.nure.vasyliev.prodef.rest.apis.PomodoroApi
import com.nure.vasyliev.prodef.rest.payloads.pomodoro.PomodoroPayload

class PomodoroRepository {

    suspend fun getAllUserPomodoro(userId: String): Pomodoros {
        val apiService = ApiClient.create().create(PomodoroApi::class.java)
        val response = apiService.getAllUserPomodoro(userId)

        return if (response.isSuccessful) {
            response.body() ?: Pomodoros(listOf())
        } else if (response.code() == 401) {
            throw RuntimeException("Invalid token")
        } else {
            Pomodoros(listOf())
        }
    }

    suspend fun createPomodoro(userId: String, taskName: String, durationMins: Int) {
        val apiService = ApiClient.create().create(PomodoroApi::class.java)
        val payload = PomodoroPayload(taskName, durationMins)
        val response = apiService.createPomodoro(userId, payload)

        if (!response.isSuccessful) {
            throw RuntimeException("Invalid token")
        }
    }
}