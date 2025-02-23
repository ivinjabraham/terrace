package com.example.terrace.features.auth.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.terrace.features.auth.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.HttpException
import javax.inject.Inject
import com.example.terrace.core.auth.SessionManager

// Define login state data class
data class LoginState(
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState

    fun updateEmail(newEmail: String) {
        _username.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        Log.d("LoginViewModel", newPassword)
        _password.value = newPassword
    }

    fun login() {
        viewModelScope.launch {
            _loginState.value = LoginState(isLoading = true)
            try {
                val response = repository.login(
                    username = _username.value,
                    password = _password.value
                )
                if (response.isSuccessful) {
                    // Verify the response structure matches your API
                    val token = response.body()?.token ?: throw Exception("No token in response")
                    sessionManager.saveAuthToken(token)
                    _loginState.value = LoginState(isSuccess = true)
                } else {
                    // Handle specific error codes
                    val errorMsg = when (response.code()) {
                        401 -> "Invalid credentials"
                        500 -> "Server error"
                        else -> "Login failed: ${response.code()}"
                    }
                    _loginState.value = LoginState(error = errorMsg)
                }
            } catch (e: Exception) {
                _loginState.value = LoginState(error = "Network error: ${e.message}")
            }
        }
    }
}