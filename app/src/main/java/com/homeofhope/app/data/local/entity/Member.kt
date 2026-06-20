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
    val password: String,
    val memberNumber: String = "",
    val joinDate: Long = System.currentTimeMillis(),
    // Saved payment info
    val savedBankName: String? = null,
    val savedCardNumber: String? = null,
    val savedExpiryDate: String? = null
)
