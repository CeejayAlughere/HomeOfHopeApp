package com.homeofhope.app.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.homeofhope.app.data.model.Volunteer
import com.homeofhope.app.data.repository.HomeRepository
import kotlinx.coroutines.launch

class VolunteerViewModel(private val repository: HomeRepository) : ViewModel() {
    var uiState by mutableStateOf<UiState>(UiState.Idle)
        private set

    fun submitVolunteer(name: String, email: String, reason: String) {
        viewModelScope.launch {
            uiState = UiState.Loading
            uiState = try {
                val response = repository.submitVolunteer(Volunteer(name = name, email = email, reason = reason))
                if (response.success) UiState.Success else UiState.Error(response.message)
            } catch (e: Exception) {
                UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun resetState() {
        uiState = UiState.Idle
    }
}
