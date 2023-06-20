package com.nure.vasyliev.prodef.service

import android.content.Context

class PomodoroSharedPrefs(
    context: Context
) {

    private val sharedPrefs = context.getSharedPreferences(POMODORO_SHARED_PREFS, Context.MODE_PRIVATE)

    fun getMillis(): Long {
        return sharedPrefs.getLong(MILLIS, 0L)
    }

    fun putMillis(millis: Long) {
        sharedPrefs.edit().putLong(MILLIS, millis).apply()
    }

    fun getMaxMillis(): Long {
        return sharedPrefs.getLong(MAX_MILLIS, 0L)
    }

    fun putMaxMillis(millis: Long) {
        sharedPrefs.edit().putLong(MAX_MILLIS, millis).apply()
    }

    companion object {
        private const val POMODORO_SHARED_PREFS = "pomodoro_shared_prefs"
        private const val MILLIS = "millis"
        private const val MAX_MILLIS = "max_millis"

    }
}