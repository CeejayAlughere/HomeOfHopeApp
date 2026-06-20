package com.homeofhope.app.data.repository

import com.homeofhope.app.data.local.dao.AnnouncementDao
import com.homeofhope.app.data.local.entity.Announcement
import kotlinx.coroutines.flow.Flow

class AnnouncementRepository(private val announcementDao: AnnouncementDao) {
    fun getAllAnnouncements(): Flow<List<Announcement>> = announcementDao.getAllAnnouncements()

    suspend fun insertAnnouncement(announcement: Announcement) =
        announcementDao.insertAnnouncement(announcement)
}
