package com.homeofhope.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "members")
data class Member(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val fullName: String,
    val idNumber: String,
    val username: String,
    val password: String
)
