package com.example.terrace.core.auth

import android.content.Context
import androidx.core.content.edit

class SessionManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("TERRACE_AUTH", Context.MODE_PRIVATE)

    fun saveAuthToken(token: String) {
        sharedPreferences.edit {
            putString("JWT_TOKEN", token)
        }
    }

    fun getAuthToken(): String? {
        return sharedPreferences.getString("JWT_TOKEN", null)
    }

    fun clearAuthToken() {
        sharedPreferences.edit {
            remove("JWT_TOKEN")
        }
    }
} 