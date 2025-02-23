package com.example.terrace.core.network.repository

import android.util.Log
import com.example.terrace.core.auth.SessionManager
import com.example.terrace.core.network.ApiService
import com.example.terrace.core.network.models.UsageRequest
import com.example.terrace.core.network.models.UsageResponse
import javax.inject.Inject

interface UsageRepository {
    suspend fun sendUsage(milliseconds: Int): Result<UsageResponse>
}

class UsageRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val sessionManager: SessionManager // Inject SessionManager
) : UsageRepository {

    override suspend fun sendUsage(milliseconds: Int): Result<UsageResponse> {
        return try {
            val request = UsageRequest(screentime = milliseconds)
            val token = sessionManager.getAuthToken()// Get token from SessionManager

            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("Auth token not found"))
            }

            val response = api.sendUsage(token = "Bearer $token", request = request) // API call

            if (response.isSuccessful) {
                Log.d("Success", "sendUsage: ${response.body()}")
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Log.d("ERROR", "${response.code()} ${response.message()}")
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("Exception", "sendUsage failed", e)
            Result.failure(e)
        }
    }
}
