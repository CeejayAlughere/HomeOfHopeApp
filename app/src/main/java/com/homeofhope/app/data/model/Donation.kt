package com.homeofhope.app.data.model

data class Donation(
    val id: Int = 0,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val amount: Double,
    val frequency: String,
    val paymentMethod: String,
    val message: String? = null,
    val donatedAt: String = ""
)
