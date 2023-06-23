package com.nure.vasyliev.prodef.rest.payloads.user

import com.google.gson.annotations.SerializedName

data class UpdatePasswordPayload(
    @SerializedName("password") val password: String
)