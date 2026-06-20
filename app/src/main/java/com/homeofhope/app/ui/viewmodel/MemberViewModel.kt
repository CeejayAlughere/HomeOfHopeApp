package com.homeofhope.app.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.homeofhope.app.data.local.entity.Member
import com.homeofhope.app.data.repository.MemberRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val member: Member) : AuthState()
    data class Error(val message: String) : AuthState()
    object Registered : AuthState()
}

class MemberViewModel(private val repository: MemberRepository) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    var currentUser by mutableStateOf<Member?>(null)
        private set

    private val _joinedEvents = MutableStateFlow<Set<String>>(emptySet())
    val joinedEvents: StateFlow<Set<String>> = _joinedEvents.asStateFlow()

    fun toggleJoinEvent(eventTitle: String) {
        val current = _joinedEvents.value
        if (current.contains(eventTitle)) {
            _joinedEvents.value = current - eventTitle
        } else {
            _joinedEvents.value = current + eventTitle
        }
    }

    fun updatePaymentInfo(bankName: String, cardNumber: String, expiryDate: String) {
        val user = currentUser ?: return
        viewModelScope.launch {
            val updatedUser = user.copy(
                savedBankName = bankName,
                savedCardNumber = cardNumber,
                savedExpiryDate = expiryDate
            )
            repository.updateMember(updatedUser)
            currentUser = updatedUser
        }
    }

    fun register(fullName: String, idNumber: String, username: String, password: String, confirmPass: String) {
        if (fullName.isBlank() || idNumber.isBlank() || username.isBlank() || password.isBlank() || confirmPass.isBlank()) {
            _authState.value = AuthState.Error("All fields are required")
            return
        }

        if (idNumber.length != 13 || !idNumber.all { it.isDigit() }) {
            _authState.value = AuthState.Error("ID number must be 13 digits")
            return
        }

        if (password != confirmPass) {
            _authState.value = AuthState.Error("Passwords do not match")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val existing = repository.getMemberByUsername(username)
            if (existing != null) {
                _authState.value = AuthState.Error("Username already exists")
            } else {
                val newMember = Member(
                    fullName = fullName,
                    idNumber = idNumber,
                    username = username,
                    password = password
                )
                repository.registerMember(newMember)
                _authState.value = AuthState.Registered
            }
        }
    }

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Please enter username and password")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val member = repository.getMemberByUsername(username)
            if (member != null && member.password == password) {
                currentUser = member
                _authState.value = AuthState.Success(member)
            } else {
                _authState.value = AuthState.Error("Invalid username or password")
            }
        }
    }

    fun clearAuthState() {
        _authState.value = AuthState.Idle
    }
    
    fun logout() {
        currentUser = null
        _authState.value = AuthState.Idle
    }
}
