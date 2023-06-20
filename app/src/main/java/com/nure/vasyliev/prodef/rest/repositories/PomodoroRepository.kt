package com.nure.vasyliev.prodef.rest.repositories

import com.nure.vasyliev.prodef.model.pomodoro.Pomodoros
import com.nure.vasyliev.prodef.rest.ApiClient
import com.nure.vasyliev.prodef.rest.apis.PomodoroApi

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
}