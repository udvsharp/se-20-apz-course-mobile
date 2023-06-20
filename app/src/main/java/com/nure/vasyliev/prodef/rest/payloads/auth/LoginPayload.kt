package com.nure.vasyliev.prodef.rest.payloads.auth

import com.google.gson.annotations.SerializedName

data class LoginPayload(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)