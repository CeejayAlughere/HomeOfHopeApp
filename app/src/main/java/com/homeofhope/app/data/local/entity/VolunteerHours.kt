package com.homeofhope.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "volunteer_hours")
data class VolunteerHours(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val memberId: Int,
    val eventName: String,
    val hours: Double,
    val serviceDate: Long,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
