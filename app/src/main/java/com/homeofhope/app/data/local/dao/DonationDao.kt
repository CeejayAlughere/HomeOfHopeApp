package com.homeofhope.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.homeofhope.app.data.local.entity.DonationRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface DonationDao {
    @Insert
    suspend fun insertDonation(donation: DonationRecord)

    @Query("SELECT * FROM donations WHERE memberId = :memberId ORDER BY date DESC")
    fun getDonationsForMember(memberId: Int): Flow<List<DonationRecord>>
}
