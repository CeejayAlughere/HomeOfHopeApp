package com.homeofhope.app.data.repository

import com.homeofhope.app.data.local.dao.CommunityDao
import com.homeofhope.app.data.local.entity.CommunityMessage
import kotlinx.coroutines.flow.Flow

class CommunityRepository(private val communityDao: CommunityDao) {
    val allMessages: Flow<List<CommunityMessage>> = communityDao.getAllMessages()

    suspend fun sendMessage(message: CommunityMessage) {
        communityDao.insertMessage(message)
    }
}
