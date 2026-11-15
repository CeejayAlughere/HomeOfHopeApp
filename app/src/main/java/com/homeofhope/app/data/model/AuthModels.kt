package com.homeofhope.app.data.model

data class LoginRequest(
    val identifier: String,
    val password: String
)

data class RegisterRequest(
    val fullName: String,
    val idNumber: String,
    val email: String,
    val username: String = email,
    val password: String
)

data class AuthUserDto(
    val id: Int,
    val fullName: String,
    val idNumber: String,
    val username: String,
    val email: String,
    val memberNumber: String,
    val status: String,
    val joinDate: Long
)

data class AuthResponse(
    val success: Boolean = false,
    val message: String = "",
    val token: String? = null,
    val user: AuthUserDto? = null
)
