// UsageViewModel.kt
package com.example.terrace.features.stats.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.terrace.core.network.models.UsageResponse
import com.example.terrace.core.network.repository.UsageRepository
import com.example.terrace.features.stats.screen.YesUsageComponent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UsageUiState {
    data object Initial : UsageUiState()
    data object Loading : UsageUiState()
    data class Success(val usage: UsageResponse) : UsageUiState()
    data class Error(val message: String) : UsageUiState()
}

@HiltViewModel
class UsageViewModel @Inject constructor(
    private val repository: UsageRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UsageUiState>(UsageUiState.Initial)
    val state: StateFlow<UsageUiState> = _state.asStateFlow()

    private val _usageData = MutableLiveData<Long>()
    val usageData: LiveData<Long> = _usageData

    fun updateUsage(milliseconds: Long) {
        viewModelScope.launch {
            _state.value = UsageUiState.Loading
            val result = repository.sendUsage(milliseconds.toInt()) // Convert Long to Int
            _state.value = when {
                result.isSuccess -> UsageUiState.Success(result.getOrNull()!!)
                result.isFailure -> UsageUiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
                else -> UsageUiState.Error("Unknown error")
            }
    }




    }

    fun fetchUsageStats(context: Context, i: Int) {
        viewModelScope.launch {
            try {
                val screenTime = YesUsageComponent(context, i)
                _usageData.postValue(screenTime)
                updateUsage(screenTime) // Update usage state after fetching
            } catch (e: Exception) {
                _state.value = UsageUiState.Error(e.message ?: "Failed to fetch usage stats")
            }
        }
    }
}
