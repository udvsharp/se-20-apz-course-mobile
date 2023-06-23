package com.nure.vasyliev.prodef.rest.repositories

import com.nure.vasyliev.prodef.model.user.User
import com.nure.vasyliev.prodef.rest.ApiClient
import com.nure.vasyliev.prodef.rest.apis.UserApi
import com.nure.vasyliev.prodef.rest.payloads.user.UpdatePasswordPayload
import com.nure.vasyliev.prodef.rest.payloads.user.UpdateUserPayload

class UserRepository {

    suspend fun getUser(userId: String): User? {
        val apiService = ApiClient.create().create(UserApi::class.java)
        val response = apiService.getUser(userId)
        return response.body()?.user
    }

    suspend fun updateUser(userId: String, user: UpdateUserPayload): User? {
        val apiService = ApiClient.create().create(UserApi::class.java)
        val response = apiService.updateUser(userId, user)

        return if (response.isSuccessful) {
            response.body()?.user
        } else {
            throw RuntimeException("Something went wrong")
        }
    }

    suspend fun updatePassword(userId: String, passwordPayload: UpdatePasswordPayload) {

    }
}