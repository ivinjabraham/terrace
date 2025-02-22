package com.example.terrace.features.auth.repository

import com.example.terrace.core.network.ApiService
import com.example.terrace.core.network.models.LoginRequest
import javax.inject.Inject

class AuthRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun login(email: String, password: String) = apiService.login(LoginRequest(email, password))
}
