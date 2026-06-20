package com.homeofhope.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "event_joins",
    primaryKeys = ["memberId", "eventId"],
    foreignKeys = [
        ForeignKey(
            entity = Member::class,
            parentColumns = ["id"],
            childColumns = ["memberId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Event::class,
            parentColumns = ["id"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("memberId"), Index("eventId")]
)
data class EventJoin(
    val memberId: Int,
    val eventId: Int,
    val hoursCompleted: Double = 0.0
)
