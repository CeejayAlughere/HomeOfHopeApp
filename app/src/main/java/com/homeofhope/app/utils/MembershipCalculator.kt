package com.homeofhope.app.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.homeofhope.app.data.model.Badge
import com.homeofhope.app.data.model.MembershipLevel

object MembershipCalculator {

    fun calculateLevel(totalDonations: Double, totalHours: Double, eventsAttended: Int): MembershipLevel {
        return MembershipLevel.values().reversed().find {
            totalDonations >= it.minDonation &&
            totalHours >= it.minHours &&
            eventsAttended >= it.minEvents
        } ?: MembershipLevel.SEED_SUPPORTER
    }

    fun getBadges(
        totalDonations: Double,
        totalHours: Double,
        eventsAttended: Int,
        firstDonationDate: Long?,
        membershipLevel: MembershipLevel
    ): List<Badge> {
        return listOf(
            Badge(
                "First Donation",
                "Your first contribution to the cause.",
                Icons.Default.Favorite,
                firstDonationDate != null
            ),
            Badge(
                "Volunteer Hero",
                "Dedicated more than 50 hours of service.",
                Icons.Default.VolunteerActivism,
                totalHours >= 50
            ),
            Badge(
                "Event Explorer",
                "Attended more than 5 community events.",
                Icons.Default.Explore,
                eventsAttended >= 5
            ),
            Badge(
                "Community Star",
                "Highly active in the Home of Hope community.",
                Icons.Default.Star,
                eventsAttended >= 10 && totalHours >= 20
            ),
            Badge(
                "Gold Supporter",
                "Achieved Gold Supporter membership level.",
                Icons.Default.WorkspacePremium,
                membershipLevel == MembershipLevel.GOLD_SUPPORTER || membershipLevel == MembershipLevel.LEGACY_SUPPORTER
            ),
            Badge(
                "Legacy Champion",
                "Achieved the highest tier of membership.",
                Icons.Default.MilitaryTech,
                membershipLevel == MembershipLevel.LEGACY_SUPPORTER
            )
        )
    }

    fun generateMemberId(): String {
        val year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
        val randomNum = (10000..99999).random()
        return "HOH-$year-$randomNum"
    }
}
