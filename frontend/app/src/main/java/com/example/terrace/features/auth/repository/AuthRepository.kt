package com.example.terrace.features.auth.repository

import com.example.terrace.core.network.ApiService
import com.example.terrace.core.network.models.LoginRequest
import com.example.terrace.core.network.models.RegisterRequest
import javax.inject.Inject

class AuthRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun login(email: String, password: String) = apiService.login(LoginRequest(email, password))
    suspend fun register(username: String, password: String) = apiService.register(RegisterRequest(username, password))
}
