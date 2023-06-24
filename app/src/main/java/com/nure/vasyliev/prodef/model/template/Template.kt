package com.nure.vasyliev.prodef.model.template

import com.google.gson.annotations.SerializedName

data class Template(
    @SerializedName("durationMins") val durationMins: Int,
    @SerializedName("name") val taskName: String,
    @SerializedName("id") val id: String,
    @SerializedName("user") val userId: String
)