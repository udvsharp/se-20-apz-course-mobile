package com.nure.vasyliev.prodef.rest.apis

import com.nure.vasyliev.prodef.model.user.UserItem
import com.nure.vasyliev.prodef.rest.payloads.user.UpdateUserPayload
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApi {

    @GET("api/users/{id}")
    suspend fun getUser(
        @Path("id") id: String
    ): Response<UserItem>

    @PUT("api/users/{id}")
    suspend fun updateUser(
        @Path("id") id: String,
        @Body user: UpdateUserPayload
    ): Response<UserItem>
}