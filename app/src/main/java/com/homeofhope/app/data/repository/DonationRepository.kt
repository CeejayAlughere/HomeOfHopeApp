package com.homeofhope.app.data.repository

import com.homeofhope.app.data.local.dao.DonationDao
import com.homeofhope.app.data.local.entity.DonationRecord
import kotlinx.coroutines.flow.Flow

class DonationRepository(private val donationDao: DonationDao) {
    suspend fun saveDonation(donation: DonationRecord) {
        donationDao.insertDonation(donation)
    }

    fun getDonationsForMember(memberId: Int): Flow<List<DonationRecord>> {
        return donationDao.getDonationsForMember(memberId)
    }
}
