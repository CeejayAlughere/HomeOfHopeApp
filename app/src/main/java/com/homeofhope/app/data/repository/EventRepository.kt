package com.homeofhope.app.data.repository

import com.homeofhope.app.data.local.dao.EventDao
import com.homeofhope.app.data.local.entity.Event
import com.homeofhope.app.data.local.entity.EventJoin
import kotlinx.coroutines.flow.Flow

class EventRepository(private val eventDao: EventDao) {
    fun getAllUpcomingEvents(): Flow<List<Event>> = eventDao.getAllUpcomingEvents()

    suspend fun insertEvent(event: Event) = eventDao.insertEvent(event)

    suspend fun joinEvent(memberId: Int, eventId: Int) {
        eventDao.joinEvent(EventJoin(memberId, eventId))
    }

    suspend fun leaveEvent(memberId: Int, eventId: Int) {
        eventDao.leaveEvent(EventJoin(memberId, eventId))
    }

    fun getJoinedEventsForMember(memberId: Int): Flow<List<Event>> =
        eventDao.getJoinedEventsForMember(memberId)
        
    fun isMemberJoined(memberId: Int, eventId: Int): Flow<Boolean> =
        eventDao.isMemberJoined(memberId, eventId)

    suspend fun updateHours(memberId: Int, eventId: Int, hours: Double) {
        eventDao.updateHours(memberId, eventId, hours)
    }

    fun getTotalHoursForMember(memberId: Int): Flow<Double?> =
        eventDao.getTotalHoursForMember(memberId)
}
