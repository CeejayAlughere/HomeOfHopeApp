package com.homeofhope.app.data.local.dao

import androidx.room.*
import com.homeofhope.app.data.local.entity.Event
import com.homeofhope.app.data.local.entity.EventJoin
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Query("SELECT * FROM events ORDER BY date ASC")
    fun getAllUpcomingEvents(): Flow<List<Event>>

    @Query("SELECT COUNT(*) FROM events WHERE title = :title")
    suspend fun getEventCountByTitle(title: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun joinEvent(eventJoin: EventJoin)

    @Delete
    suspend fun leaveEvent(eventJoin: EventJoin)

    @Query("SELECT * FROM events WHERE id IN (SELECT eventId FROM event_joins WHERE memberId = :memberId)")
    fun getJoinedEventsForMember(memberId: Int): Flow<List<Event>>
    
    @Query("SELECT EXISTS(SELECT 1 FROM event_joins WHERE memberId = :memberId AND eventId = :eventId)")
    fun isMemberJoined(memberId: Int, eventId: Int): Flow<Boolean>

    @Query("UPDATE event_joins SET hoursCompleted = :hours WHERE memberId = :memberId AND eventId = :eventId")
    suspend fun updateHours(memberId: Int, eventId: Int, hours: Double)

    @Query("SELECT SUM(hoursCompleted) FROM event_joins WHERE memberId = :memberId")
    fun getTotalHoursForMember(memberId: Int): Flow<Double?>
}
