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

// Define login state data class
data class LoginState(
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

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
        android.util.Log.d("LoginViewModel", "Login function called")
        viewModelScope.launch {
            try {
                Log.d("LoginViewModel", "Attempting network call...")
                _loginState.value = LoginState(isLoading = true)
                val response = authRepository.login(_username.value, _password.value)
                
                if (response.isSuccessful) {
                    val token = response.body()?.token
                    // Store token if needed
                    _loginState.value = LoginState(isSuccess = true)
                } else {
                    _loginState.value = LoginState(error = "Login failed: ${response}")
                    Log.d("LoginViewModel", "${response}")
                }
            } catch (e: HttpException) {
                _loginState.value = LoginState(error = "Network error: ${e.message}")
            } catch (e: Exception) {
                _loginState.value = LoginState(error = "Unexpected error: ${e.message}")
            }
        }
    }
}