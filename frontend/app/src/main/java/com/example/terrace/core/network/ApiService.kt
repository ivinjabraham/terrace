package com.example.terrace.core.network

import com.example.terrace.core.network.models.LeaderboardResponse
import com.example.terrace.core.network.models.LoginRequest
import com.example.terrace.core.network.models.LoginResponse
import com.example.terrace.core.network.models.RegisterRequest
import com.example.terrace.core.network.models.RegisterResponse
import com.example.terrace.core.network.models.ScoreResponse
import com.example.terrace.core.network.models.UsageRequest
import com.example.terrace.core.network.models.UsageResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT

interface ApiService {
    @POST("/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @PUT("/api/screentime")
    suspend fun sendUsage(
        @Header("Authorization") token: String,
        @Body request: UsageRequest
    ): Response<UsageResponse>

    @GET("/api/profile")
    suspend fun getScore(
        @Header("Authorization") token: String,
    ): Response<ScoreResponse>

    @GET("/api/leaderboard")
    suspend fun getLeaderboard(
        @Header("Authorization") authHeader: String
    ): Response<List<LeaderboardResponse>>
}