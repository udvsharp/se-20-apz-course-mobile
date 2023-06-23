package com.nure.vasyliev.prodef.service

import android.content.Context

class PomodoroSharedPrefs(
    context: Context
) {

    private val sharedPrefs = context.getSharedPreferences(POMODORO_SHARED_PREFS, Context.MODE_PRIVATE)

    fun getStartMillis(): Long {
        return sharedPrefs.getLong(START_MILLIS, 0L)
    }

    fun putStartMillis(millis: Long) {
        sharedPrefs.edit().putLong(START_MILLIS, millis).apply()
    }

    fun getTaskName(): String {
        return sharedPrefs.getString(TASK_NAME, "") ?: ""
    }

    fun putTaskName(taskName: String) {
        sharedPrefs.edit().putString(TASK_NAME, taskName).apply()
    }

    fun getMaxMillis(): Long {
        return sharedPrefs.getLong(MAX_MILLIS, 0L)
    }

    fun putMaxMillis(millis: Long) {
        sharedPrefs.edit().putLong(MAX_MILLIS, millis).apply()
    }

    fun getIsStarted(): Boolean {
        return sharedPrefs.getBoolean(IS_STARTED, false)
    }

    fun putIsStarted(isStarted: Boolean) {
        sharedPrefs.edit().putBoolean(IS_STARTED, isStarted).apply()
    }

    fun getPomodoroId(): String {
        return sharedPrefs.getString(POMODORO_ID, "") ?: ""
    }

    fun putPomodoroId(pomodoroId: String) {
        sharedPrefs.edit().putString(POMODORO_ID, pomodoroId).apply()
    }

    companion object {
        private const val POMODORO_SHARED_PREFS = "pomodoro_shared_prefs"
        private const val START_MILLIS = "start_millis"
        private const val TASK_NAME = "task_name"
        private const val MAX_MILLIS = "max_millis"
        private const val IS_STARTED = "is_started"
        private const val POMODORO_ID = "pomodoro_id"
    }
}