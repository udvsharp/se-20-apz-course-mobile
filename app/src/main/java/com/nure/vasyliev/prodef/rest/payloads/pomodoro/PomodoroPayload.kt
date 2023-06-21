package com.nure.vasyliev.prodef.rest.payloads.pomodoro

import com.google.gson.annotations.SerializedName

data class PomodoroPayload(
    @SerializedName("task") val task: String,
    @SerializedName("durationMins") val durationMins: Int
)