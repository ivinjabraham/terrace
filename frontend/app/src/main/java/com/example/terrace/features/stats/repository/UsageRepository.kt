package com.example.terrace.core.network.repository

import android.util.Log
import com.example.terrace.core.network.ApiService
import com.example.terrace.core.network.models.UsageRequest
import com.example.terrace.core.network.models.UsageResponse
import javax.inject.Inject
import kotlin.math.log

interface UsageRepository {
    suspend fun sendUsage(milliseconds: Int): Result<UsageResponse>
}

class UsageRepositoryImpl @Inject constructor(
    private val api: ApiService
) : UsageRepository {
    override suspend fun sendUsage(milliseconds: Int): Result<UsageResponse> {
        return try {
            val request = UsageRequest(screentime = milliseconds)
            val response = api.sendUsage(request = request, token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3NDAzNzQ4MTksInVzZXJuYW1lIjoiaGVtYW4ifQ.UvYx0UfRdU7GfkraWnbmTzu-2kTH0mv1WXqHdH59atI")

            if (response.isSuccessful) {
                Log.d("Success: ", "sendUsage: ${response.body()}")
                response.body()?.let {

                    Result.success(it) // Correctly return Result<UsageResponse>
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Log.d("ERROR: ", " ${ response.code()} ${response.message()}")
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}