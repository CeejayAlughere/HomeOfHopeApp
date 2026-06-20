package com.homeofhope.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.homeofhope.app.data.local.entity.ImpactStory
import kotlinx.coroutines.flow.Flow

@Dao
interface ImpactStoryDao {
    @Query("SELECT * FROM impact_stories ORDER BY date DESC")
    fun getAllImpactStories(): Flow<List<ImpactStory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImpactStory(story: ImpactStory)
}
