package com.homeofhope.app.data.model

data class Volunteer(
    val id: Int = 0,
    val name: String,
    val email: String,
    val reason: String,
    val signedUpAt: String = ""
)
