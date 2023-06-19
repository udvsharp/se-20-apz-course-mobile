package com.nure.vasyliev.prodef.model.user

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("name") val name: String,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("roles") val roles: List<String>,
    @SerializedName("id") val id: String
)