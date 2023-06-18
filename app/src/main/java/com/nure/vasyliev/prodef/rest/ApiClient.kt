package com.nure.vasyliev.prodef.rest

import com.google.gson.Gson
import com.nure.vasyliev.prodef.BuildConfig
import com.nure.vasyliev.prodef.model.User
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import com.nure.vasyliev.prodef.rest.payload.LoginPayload
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

object ApiClient {
    private const val BASE_URL = BuildConfig.BASE_URL

    fun create(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }

    suspend fun login(email: String, password: String): Pair<User?, String?> {
        val apiService = create()
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

            val json = Json.parseToJsonElement(response.body().toString()).jsonObject

            if(!json.containsKey("user")  || !json.containsKey("token")) {
                return Pair(null, null)
            }

            val user = Json.decodeFromJsonElement<User>(json["user"]!!)
            val token = json["token"]?.jsonPrimitive?.content
            Pair(user, token)
        } catch (e: Exception) {
            return Pair(null, null)
        }
    }
}
