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

    var historyError by mutableStateOf<String?>(null)
        private set
    var historyLoading by mutableStateOf(false)
        private set

    fun refreshHistory(memberId: Int) {
        if (historyLoading) return
        historyLoading = true
        historyError = null
        viewModelScope.launch {
            try { donationRepository.refreshHistory(memberId) } catch (e: kotlinx.coroutines.CancellationException) { throw e } catch (e: Exception) { historyError = e.message ?: "Unable to load history." } finally { historyLoading = false }
        }
    }

    fun submitDonation(
        causeId: Int?,
        bankName: String,
        amount: String,
        memberId: Int?,
        isWebsiteSession: Boolean,
        clientRequestId: String
    ) {
        if (uiState is UiState.Loading || uiState is UiState.Success) return

        if (!isWebsiteSession || memberId == null) {
            uiState = UiState.Error("Please sign in to record a donation.")
            return
        }

        if (causeId == null || causeId <= 0) {
            uiState = UiState.Error("Please select a cause.")
            return
        }

        if (bankName.isBlank()) {
            uiState = UiState.Error("Please select your bank.")
            return
        }

        val donationAmount = amount.trim().toBigDecimalOrNull()
        if (donationAmount == null ||
            donationAmount.signum() <= 0 ||
            donationAmount > 1_000_000.toBigDecimal() ||
            donationAmount.stripTrailingZeros().scale() > 2
        ) {
            uiState = UiState.Error(
                "Enter an amount up to R1,000,000 using no more than two decimal places."
            )
            return
        }

        // Set this before launching so a second click sees Loading immediately.
        uiState = UiState.Loading

        viewModelScope.launch {
            try {
                donationRepository.submitCauseDonation(
                    causeId = causeId,
                    bankName = bankName,
                    amount = donationAmount.toDouble(),
                    clientRequestId = clientRequestId
                )
                uiState = UiState.Success
                refreshHistory(memberId)
            } catch (e: kotlinx.coroutines.CancellationException) {
                throw e
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
        if (uiState !is UiState.Loading) {
            uiState = UiState.Idle
        }
    }
}
