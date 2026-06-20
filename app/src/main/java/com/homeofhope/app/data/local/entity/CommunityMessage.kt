package com.homeofhope.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "community_messages")
data class CommunityMessage(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val memberId: Int,
    val memberName: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)
