package com.example.terrace.features.home.model

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
            val token = sessionManager.getAuthToken() // Fetch token from session
            if (token != null) {
                val response = apiService.getScore(token)
                if (response.isSuccessful) {
                    _score.value = response.body()
                }
            }
        }
    }
}
