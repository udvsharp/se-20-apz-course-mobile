package com.nure.vasyliev.prodef.rest.apis

import com.nure.vasyliev.prodef.model.user.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApi {

    @GET("api/users")
    suspend fun getUser(
        @Query("id") id: String
    ): Response<User>
}