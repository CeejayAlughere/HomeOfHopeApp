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
    val joinDate: Long,
    val phoneNumber: String = ""
)

data class AuthResponse(
    val success: Boolean = false,
    val message: String = "",
    val token: String? = null,
    val user: AuthUserDto? = null,
    val action: String? = null,
    val email: String? = null,
    val displayName: String? = null
)


data class GoogleLoginRequest(
    val idToken: String,
    val fullName: String = "",
    val idNumber: String = "",
    val password: String = ""
)

data class RemoteDonation(
    val id: Int,
    val amount: Double,
    val bankName: String,
    val date: Long,
    val causeTitle: String = ""
)

data class DonationHistoryResponse(
    val success: Boolean = false,
    val message: String = "",
    val user: AuthUserDto? = null,
    val donations: List<RemoteDonation> = emptyList()
)

data class ProfileUpdateRequest(val fullName: String, val phoneNumber: String)
