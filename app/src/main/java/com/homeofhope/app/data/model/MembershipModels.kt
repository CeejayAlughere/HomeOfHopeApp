package com.homeofhope.app.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class MembershipLevel(
    val label: String,
    val minDonation: Double,
    val minHours: Double,
    val minEvents: Int
) {
    SEED_SUPPORTER("Seed Supporter", 0.0, 0.0, 0),
    HOPE_SUPPORTER("Hope Supporter", 500.0, 10.0, 1),
    SILVER_SUPPORTER("Silver Supporter", 2000.0, 50.0, 5),
    GOLD_SUPPORTER("Gold Supporter", 10000.0, 200.0, 12),
    LEGACY_SUPPORTER("Legacy Supporter", 50000.0, 500.0, 25)
}

data class Badge(
    val label: String,
    val description: String,
    val icon: ImageVector,
    val isUnlocked: Boolean = false
)
