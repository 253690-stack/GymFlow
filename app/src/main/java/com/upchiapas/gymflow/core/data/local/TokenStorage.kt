package com.upchiapas.gymflow.core.data.local

import android.content.Context
import android.content.SharedPreferences

interface TokenStorage {
    fun saveToken(token: String)
    fun getToken(): String?
    fun clearToken()
}

class TokenStorageImpl(context: Context) : TokenStorage {

    // Variable privada: solo esta clase sabe cómo acceder a SharedPreferences
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    override fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    override fun clearToken() {
        prefs.edit().remove(KEY_TOKEN).apply()
    }

    companion object {
        private const val PREFS_NAME = "gymflow_prefs"
        private const val KEY_TOKEN = "jwt_token"
    }
}


