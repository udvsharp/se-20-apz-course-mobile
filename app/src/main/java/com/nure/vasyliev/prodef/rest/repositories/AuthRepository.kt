package com.nure.vasyliev.prodef.rest.repositories

import com.nure.vasyliev.prodef.model.user.User
import com.nure.vasyliev.prodef.rest.ApiClient
import com.nure.vasyliev.prodef.rest.apis.AuthApi
import com.nure.vasyliev.prodef.rest.payloads.LoginPayload
import com.nure.vasyliev.prodef.rest.payloads.RegisterPayload

class AuthRepository {

    suspend fun signIn(email: String, password: String): Pair<User?, String?> {
        val apiService = ApiClient.create().create(AuthApi::class.java)
        val payload = LoginPayload(email, password)

        return try {
            val response = apiService.login(payload)
            if (!response.isSuccessful) {
                Pair(null, null)
            }

            val body = response.body()
            if (body == null) {
                Pair(null, null)
            }

            val user = body?.user
            val token = body?.token
            Pair(user, token)
        } catch (e: Exception) {
            return Pair(null, null)
        }
    }

    suspend fun signUp(
        name: String,
        username: String,
        email: String,
        password: String
    ): Pair<User?, String?> {
        val apiService = ApiClient.create().create(AuthApi::class.java)
        val payload = RegisterPayload(name, username, email, password)

        return try {
            val response = apiService.register(payload)
            if (!response.isSuccessful) {
                Pair(null, null)
            }

            val body = response.body()
            if (body == null) {
                Pair(null, null)
            }

            val user = body?.user
            val token = body?.token
            Pair(user, token)
        } catch (e: Exception) {
            return Pair(null, null)
        }
    }
}