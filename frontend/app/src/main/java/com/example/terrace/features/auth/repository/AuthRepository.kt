package com.example.terrace.features.auth.repository

import com.example.terrace.core.network.ApiService
import com.example.terrace.core.network.models.LoginRequest
import com.example.terrace.features.auth.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*;
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import org.json.JSONObject
import javax.inject.Inject

class AuthRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun login(email: String, password: String) = apiService.login(LoginRequest(email, password))
}
