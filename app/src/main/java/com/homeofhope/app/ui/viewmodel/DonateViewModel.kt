package com.homeofhope.app.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.homeofhope.app.data.model.Donation
import com.homeofhope.app.data.repository.HomeRepository
import kotlinx.coroutines.launch

class DonateViewModel(private val repository: HomeRepository) : ViewModel() {
    var uiState by mutableStateOf<UiState>(UiState.Idle)
        private set

    fun submitDonation(
        name: String,
        email: String,
        phoneNumber: String,
        amount: Double,
        frequency: String,
        paymentMethod: String,
        message: String?
    ) {
        viewModelScope.launch {
            uiState = UiState.Loading
            uiState = try {
                val donation = Donation(
                    name = name,
                    email = email,
                    phoneNumber = phoneNumber,
                    amount = amount,
                    frequency = frequency,
                    paymentMethod = paymentMethod,
                    message = message
                )
                val response = repository.submitDonation(donation)
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
