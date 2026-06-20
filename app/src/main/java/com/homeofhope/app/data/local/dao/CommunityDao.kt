package com.homeofhope.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.homeofhope.app.data.local.entity.CommunityMessage
import kotlinx.coroutines.flow.Flow

@Dao
interface CommunityDao {
    @Query("SELECT * FROM community_messages ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<CommunityMessage>>

    @Insert
    suspend fun insertMessage(message: CommunityMessage)
}
