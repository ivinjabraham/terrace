package com.example.terrace.core.network

import com.example.terrace.core.network.models.LoginRequest
import com.example.terrace.core.network.models.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response

interface ApiService {
    @POST("/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}