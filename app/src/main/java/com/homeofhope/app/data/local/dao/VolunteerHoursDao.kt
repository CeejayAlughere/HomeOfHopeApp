package com.homeofhope.app.data.local.dao

import androidx.room.*
import com.homeofhope.app.data.local.entity.VolunteerHours
import kotlinx.coroutines.flow.Flow

@Dao
interface VolunteerHoursDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHours(hours: VolunteerHours)

    @Update
    suspend fun updateHours(hours: VolunteerHours)

    @Delete
    suspend fun deleteHours(hours: VolunteerHours)

    @Query("SELECT * FROM volunteer_hours WHERE memberId = :memberId ORDER BY serviceDate DESC")
    fun getHoursByMember(memberId: Int): Flow<List<VolunteerHours>>

    @Query("SELECT SUM(hours) FROM volunteer_hours WHERE memberId = :memberId")
    fun getTotalHoursByMember(memberId: Int): Flow<Double?>

    @Query("SELECT SUM(hours) FROM volunteer_hours")
    fun getTotalHoursAll(): Flow<Double?>
}
