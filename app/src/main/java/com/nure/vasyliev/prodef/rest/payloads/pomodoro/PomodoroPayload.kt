package com.nure.vasyliev.prodef.rest.payloads.pomodoro

data class PomodoroPayload(
    val durationMins: Int,
    val task: String
)