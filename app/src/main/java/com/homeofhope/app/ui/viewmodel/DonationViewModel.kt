package com.homeofhope.app.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.homeofhope.app.data.local.entity.DonationRecord
import com.homeofhope.app.data.repository.DonationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class DonationViewModel(
    private val donationRepository: DonationRepository
) : ViewModel() {

    var uiState by mutableStateOf<UiState>(UiState.Idle)
        private set

    fun submitDonation(
        causeId: Int?,
        bankName: String,
        amount: String,
        memberId: Int?,
        isWebsiteSession: Boolean
    ) {
        val donationAmount = amount.toDoubleOrNull()

        if (!isWebsiteSession || memberId == null) {
            uiState = UiState.Error("Please sign in with your website account to record a donation. Google sign-in is not linked to website donations yet.")
            return
        }

        if (causeId == null || causeId <= 0) {
            uiState = UiState.Error("Please select a cause")
            return
        }

        if (bankName.isBlank()) {
            uiState = UiState.Error("Please select your bank")
            return
        }

        if (donationAmount == null || !donationAmount.isFinite() || donationAmount <= 0 || donationAmount > 1_000_000) {
            uiState = UiState.Error("Enter an amount greater than R0 and up to R1,000,000")
            return
        }

        viewModelScope.launch {
            uiState = UiState.Loading

            try {
                donationRepository.submitCauseDonation(
                    causeId = causeId,
                    bankName = bankName,
                    amount = donationAmount
                )
                // The server has accepted the record. Cache it for this device only.
                // A cache failure must not report server failure and invite a duplicate.
                runCatching {
                    donationRepository.saveDonation(
                        DonationRecord(
                            memberId = memberId,
                            amount = donationAmount,
                            date = System.currentTimeMillis(),
                            bankName = bankName
                        )
                    )
                }
                uiState = UiState.Success
            } catch (e: Exception) {
                uiState = UiState.Error(
                    e.message ?: "Unable to submit your donation. Please try again."
                )
            }
        }
    }

    fun getDonationsForMember(memberId: Int): Flow<List<DonationRecord>> {
        return donationRepository.getDonationsForMember(memberId)
    }

    fun resetState() {
        uiState = UiState.Idle
    }
}
