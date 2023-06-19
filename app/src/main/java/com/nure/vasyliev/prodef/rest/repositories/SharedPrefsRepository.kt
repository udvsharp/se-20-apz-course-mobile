package com.nure.vasyliev.prodef.rest.repositories

import android.content.Context

class SharedPrefsRepository(
    context: Context
) {

    private val sharedPrefs = context.getSharedPreferences(USER_SHARED_PREFS, Context.MODE_PRIVATE)

    suspend fun getUserToken(): String? {
        return sharedPrefs.getString(TOKEN, "")
    }

    suspend fun putUserToken(token: String?) {
        sharedPrefs.edit().putString(TOKEN, token).apply()
    }

    suspend fun getUserId(): String? {
        return sharedPrefs.getString(USER_ID, "")
    }

    suspend fun putUserId(id: String?) {
        sharedPrefs.edit().putString(USER_ID, id).apply()
    }

    companion object {
        private const val USER_SHARED_PREFS = "user_shared_prefs"
        private const val TOKEN = "token"
        private const val USER_ID = "user_id"
    }
}