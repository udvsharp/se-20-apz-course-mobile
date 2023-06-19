package com.nure.vasyliev.prodef.rest.payloads

import com.google.gson.annotations.SerializedName

data class RegisterPayload(
    @SerializedName("name") val name: String,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)
