package com.example.terrace.features.home.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.terrace.core.auth.SessionManager
import com.example.terrace.core.network.ApiService
import com.example.terrace.core.network.models.ScoreResponse

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _score = MutableStateFlow<ScoreResponse?>(null)
    val score: StateFlow<ScoreResponse?> get() = _score

    fun fetchScore() {
        viewModelScope.launch {
            val token = sessionManager.getAuthToken()
            Log.d("AAA", "Token: $token") // Check if token is null

            if (token != null) {
                try {
                    val response = apiService.getScore("Bearer $token")

                    Log.d("AAA", "Raw Response: ${response.raw()}") // Print raw response
                    Log.d("AAA", "Response Code: ${response.code()}") // Print status code

                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.d("AAA", "fetchScore success: $responseBody")

                        _score.value = responseBody
                    } else {
                        Log.e("AAA", "fetchScore failed: ${response.errorBody()?.string()}")
                    }
                } catch (e: Exception) {
                    Log.e("AAA", "fetchScore exception: ${e.message}", e)
                }
            } else {
                Log.e("AAA", "fetchScore: Token is null")
            }
        }
    }


}
