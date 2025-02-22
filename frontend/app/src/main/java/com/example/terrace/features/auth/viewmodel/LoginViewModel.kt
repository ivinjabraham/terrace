package com.example.terrace.features.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.terrace.features.auth.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _loginResult = MutableStateFlow<String?>(null)
    val loginResult: StateFlow<String?> = _loginResult

    fun updateEmail(newEmail: String) {
        _username.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = authRepository.login(email, password)
                if (response.isSuccessful) {
                    val token = response.body()?.token
                    print("Login successful")
                } else {
                    print("Login failed")
                }
            } catch (e: HttpException) {
                print("Oops")
            }
        }
    }
}