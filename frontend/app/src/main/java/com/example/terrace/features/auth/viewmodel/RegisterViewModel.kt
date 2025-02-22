package com.example.terrace.features.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.terrace.features.auth.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword

    private val _registerState = MutableStateFlow(RegisterState())
    val registerState: StateFlow<RegisterState> = _registerState

    fun updateUsername(newUsername: String) {
        _username.value = newUsername
    }

    fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }

    fun updateConfirmPassword(newConfirmPassword: String) {
        _confirmPassword.value = newConfirmPassword
    }

    fun register() {
        if (password.value != confirmPassword.value) {
            _registerState.value = RegisterState(error = "Passwords do not match")
            return
        }

        viewModelScope.launch {
            try {
                _registerState.value = RegisterState(isLoading = true)
                val response = authRepository.register(_username.value, _password.value)
                
                if (response.isSuccessful) {
                    _registerState.value = RegisterState(isSuccess = true)
                } else {
                    _registerState.value = RegisterState(error = "Registration failed: ${response.message()}")
                }
            } catch (e: HttpException) {
                _registerState.value = RegisterState(error = "Network error: ${e.message}")
            } catch (e: Exception) {
                _registerState.value = RegisterState(error = "Unexpected error: ${e.message}")
            }
        }
    }
}

data class RegisterState(
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
) 