package com.nure.vasyliev.prodef.model.pomodoro

import com.google.gson.annotations.SerializedName

data class Pomodoro(
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("durationMins") val durationMins: Int,
    @SerializedName("finishedEarlier") val finishedEarlier: Boolean,
    @SerializedName("id") val id: String,
    @SerializedName("isValid") val isValid: Boolean,
    @SerializedName("task") val task: String,
    @SerializedName("timeLeftMins") val timeLeftMins: Any,
    @SerializedName("user") val user: String
)