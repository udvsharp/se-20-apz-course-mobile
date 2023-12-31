package com.nure.vasyliev.prodef.rest.payload

import com.google.gson.annotations.SerializedName

data class LoginPayload(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)
