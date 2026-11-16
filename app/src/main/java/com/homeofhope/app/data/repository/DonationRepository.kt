package com.homeofhope.app.data.repository

import com.homeofhope.app.data.local.AuthSessionStore
import com.homeofhope.app.data.local.dao.DonationDao
import com.homeofhope.app.data.local.entity.DonationRecord
import com.homeofhope.app.data.model.CauseDonationRequest
import com.homeofhope.app.data.model.CauseDonationResponse
import com.homeofhope.app.data.network.AuthApiService
import java.io.IOException
import kotlinx.coroutines.flow.Flow

class DonationRepository(
    private val donationDao: DonationDao,
    private val authApi: AuthApiService,
    private val sessionStore: AuthSessionStore
) {
    suspend fun saveDonation(donation: DonationRecord) {
        donationDao.insertDonation(donation)
    }

    fun getDonationsForMember(memberId: Int): Flow<List<DonationRecord>> {
        return donationDao.getDonationsForMember(memberId)
    }

    suspend fun submitCauseDonation(
        causeId: Int,
        bankName: String,
        amount: Double
    ): CauseDonationResponse {
        val token = sessionStore.currentToken()
            ?: throw IOException("Please log in before making a donation.")

        val response = try {
            authApi.submitCauseDonation(
                authorization = "Bearer $token",
                body = CauseDonationRequest(
                    causeId = causeId,
                    bankName = bankName,
                    amount = amount
                )
            )
        } catch (e: IOException) {
            throw IOException("Could not confirm the donation record. Check your website donation history before trying again.", e)
        }
        val body = response.body()

        if (!response.isSuccessful || body?.success != true) {
            throw IOException(if (response.code() == 401) "Your website session has expired. Please sign out and log in again." else body?.message ?: "Unable to submit your donation (HTTP ${response.code()}).")
        }

        return body
    }
}
