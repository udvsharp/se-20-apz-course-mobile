package com.nure.vasyliev.prodef.model.pomodoro

import com.google.gson.annotations.SerializedName

data class Pomodoros(
    @SerializedName("pomodoros") val pomodoros: List<Pomodoro>
)