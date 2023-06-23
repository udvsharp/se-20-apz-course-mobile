package com.nure.vasyliev.prodef.rest.repositories

import com.nure.vasyliev.prodef.model.pomodoro.FilteredPomodoros
import com.nure.vasyliev.prodef.model.pomodoro.Pomodoro
import com.nure.vasyliev.prodef.model.pomodoro.Pomodoros
import com.nure.vasyliev.prodef.rest.ApiClient
import com.nure.vasyliev.prodef.rest.apis.PomodoroApi
import com.nure.vasyliev.prodef.rest.payloads.pomodoro.PomodoroPayload
import com.nure.vasyliev.prodef.utils.formatFromServer

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

    suspend fun getLastValidUserPomodoro(userId: String): Pomodoro? {
        val apiService = ApiClient.create().create(PomodoroApi::class.java)
        val response = apiService.getAllValidUserPomodoro(userId)


        val pomodoros = response.body() ?: FilteredPomodoros(listOf())
        return if (pomodoros.pomodoros.isEmpty()) {
            null
        } else {
            pomodoros.pomodoros.sortedBy {
                formatFromServer.parse(it.createdAt)
            }.first()
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

    suspend fun startPomodoro(userId: String) {
        val apiService = ApiClient.create().create(PomodoroApi::class.java)
        apiService.startPomodoro(userId)
    }

    suspend fun stopPomodoro(userId: String) {
        val apiService = ApiClient.create().create(PomodoroApi::class.java)
        apiService.stopPomodoro(userId)
    }
}