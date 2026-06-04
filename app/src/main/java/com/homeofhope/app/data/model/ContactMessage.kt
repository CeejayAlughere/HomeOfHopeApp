package com.homeofhope.app.data.model

data class ContactMessage(
    val id: Int = 0,
    val name: String,
    val email: String,
    val subject: String,
    val message: String,
    val submittedAt: String = ""
)
