package com.homeofhope.app.data.model

data class Donation(
    val id: Int = 0,
    val name: String,
    val email: String,
    val amount: Double,
    val donatedAt: String = ""
)
