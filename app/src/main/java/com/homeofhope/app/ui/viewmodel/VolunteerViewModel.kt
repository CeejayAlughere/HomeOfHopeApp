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

    fun submitVolunteer(volunteer: Volunteer) {
        viewModelScope.launch {
            uiState = UiState.Loading
            uiState = try {
                val response = repository.submitVolunteer(volunteer)
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
