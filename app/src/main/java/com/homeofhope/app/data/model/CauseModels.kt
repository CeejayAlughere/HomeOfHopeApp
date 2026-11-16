package com.homeofhope.app.data.model

data class Cause(
    val id: Int = 0,
    val slug: String = "",
    val title: String = "",
    val raised: Double = 0.0,
    val goal: Double = 0.0,
    val progress: Double = 0.0,
    val description: String = "",
    val imageUrl: String = ""
)

data class CausesResponse(
    val success: Boolean = false,
    val message: String = "",
    val causes: List<Cause> = emptyList()
)

data class CauseDonationRequest(
    val causeId: Int,
    val bankName: String,
    val amount: Double
)

data class CauseDonationResponse(
    val success: Boolean = false,
    val message: String = "",
    val donationId: Int? = null
)
