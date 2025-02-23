package com.example.terrace.features.leaderboard.model

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.terrace.core.auth.SessionManager
import com.example.terrace.features.leaderboard.LeaderboardEntry
import com.example.terrace.features.leaderboard.LeaderboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val repository: LeaderboardRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    val entries = mutableStateOf(emptyList<LeaderboardEntry>())
    val isLoading = mutableStateOf(true)
    val error = mutableStateOf<String?>(null)

    init {
        loadLeaderboard()
    }

    fun loadLeaderboard() {
        viewModelScope.launch {
            try {
                // Temporary debug logging
                Log.d("Leaderboard", "Auth token: ${sessionManager.getAuthToken()}")

                val apiResponse = repository.getLeaderboard()
                entries.value = apiResponse.mapIndexed { index, item ->
                    LeaderboardEntry(
                        rank = index + 1,
                        name = item.username,
                        points = item.score,
                        isCurrentUser = item.isCurrentUser
                    )
                }
                error.value = null
            } catch (e: Exception) {
                Log.e("Leaderboard", "Error loading leaderboard", e)
                error.value = e.message ?: "Failed to load leaderboard"
            } finally {
                isLoading.value = false
            }
        }
    }
}