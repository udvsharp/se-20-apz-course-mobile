package com.nure.vasyliev.prodef.rest.payloads.user

import com.google.gson.annotations.SerializedName

data class UpdateUserPayload(
    @SerializedName("name") val name: String?,
    @SerializedName("username") val username: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("roles") val roles: List<String>?,
)