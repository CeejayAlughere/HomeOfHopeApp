package com.homeofhope.app.data.repository

import com.homeofhope.app.data.local.dao.ImpactStoryDao
import com.homeofhope.app.data.local.entity.ImpactStory
import kotlinx.coroutines.flow.Flow

class ImpactStoryRepository(private val impactStoryDao: ImpactStoryDao) {
    fun getAllImpactStories(): Flow<List<ImpactStory>> = impactStoryDao.getAllImpactStories()

    suspend fun insertImpactStory(story: ImpactStory) =
        impactStoryDao.insertImpactStory(story)
}
