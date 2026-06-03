package com.example.homeofhopeapp.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.homeofhopeapp.data.User
import com.example.homeofhopeapp.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun register(fullName: String, username: String, password: String, confirmPassword: String) {
        if (password != confirmPassword) {
            _authState.value = AuthState.Error("Passwords do not match")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            if (userRepository.isUsernameTaken(username)) {
                _authState.value = AuthState.Error("Username already taken")
            } else {
                userRepository.registerUser(User(fullName = fullName, username = username, password = password))
                _authState.value = AuthState.Success("Registration successful")
            }
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val user = userRepository.loginUser(username)
            if (user != null && user.password == password) {
                _authState.value = AuthState.Authenticated(user)
            } else {
                _authState.value = AuthState.Error("Invalid username or password")
            }
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val message: String) : AuthState()
    data class Error(val message: String) : AuthState()
    data class Authenticated(val user: User) : AuthState()
}

class AuthViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
