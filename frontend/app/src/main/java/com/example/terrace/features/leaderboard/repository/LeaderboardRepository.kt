package com.example.terrace.features.leaderboard.repository

import com.example.terrace.core.network.ApiService
import com.example.terrace.core.network.models.LeaderboardResponse
import com.example.terrace.core.auth.SessionManager
import javax.inject.Inject

class LeaderboardRepository @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {
    suspend fun getLeaderboard(): List<LeaderboardResponse> {
        try {
            val token = sessionManager.getAuthToken()
                ?: throw Exception("No authentication token found - please login first")
            
            val response = apiService.getLeaderboard("Bearer $token")
            
            if (!response.isSuccessful) {
                throw Exception("API Error: ${response.code()} - ${response.errorBody()?.string()}")
            }
            
            return response.body() ?: emptyList()
            
        } catch (e: Exception) {
            throw Exception("Leaderboard request failed: ${e.message}")
        }
    }
} 