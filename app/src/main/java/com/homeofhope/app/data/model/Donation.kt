package com.homeofhope.app.data.model

data class Donation(
    val fullName: String,
    val bankName: String,
    val cardNumber: String,
    val expiryDate: String,
    val cvv: String,
    val amount: Double
)
