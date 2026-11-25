package com.homeofhope.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home      : Screen("home",      "Home",    Icons.Default.Home)
    object About     : Screen("about",     "About",   Icons.Default.Info)
    object Causes    : Screen("causes",    "Causes",  Icons.Default.Favorite)
    object Events    : Screen("events",    "Events",  Icons.Default.Event)
    object Blog      : Screen("blog",      "Blog",    Icons.Default.Article)
    object Services  : Screen("services",  "Services",Icons.Default.MedicalServices)
    object Contact   : Screen("contact",   "Contact", Icons.Default.ContactMail)
    object FAQ       : Screen("faq",       "FAQs",    Icons.Default.QuestionAnswer)
    object Help      : Screen("help",      "Help",    Icons.Default.Help)
    object Privacy   : Screen("privacy",   "Privacy", Icons.Default.PrivacyTip)
    object Terms     : Screen("terms",     "Terms",   Icons.Default.Gavel)
    object Cookies   : Screen("cookies",   "Cookies", Icons.Default.Cookie)
    object CauseDetail : Screen("cause_detail/{causeId}", "Cause Detail", Icons.Default.Favorite)
    object BlogDetail : Screen("blog_detail/{blogId}", "Blog Detail", Icons.Default.Article)
    object Donation : Screen("donation?causeId={causeId}", "Donate Now", Icons.Default.VolunteerActivism) {
        fun createRoute(causeId: Int? = null): String {
            return "donation?causeId=${causeId ?: 0}"
        }
    }

    // Membership
    object Register : Screen("register", "Become a Member", Icons.Default.PersonAdd)
    object Login : Screen("login", "Login", Icons.AutoMirrored.Filled.Login)
    object GoogleProfileCompletion : Screen(
        "google_profile_completion",
        "Complete Your Profile",
        Icons.Default.PersonAdd
    )
    object MemberDashboard : Screen("member_dashboard", "Member Dashboard", Icons.Default.Dashboard)
    object MemberDonate : Screen("member_donate", "Donate", Icons.Default.VolunteerActivism)
    object MemberVolunteer : Screen("member_volunteer", "Volunteer", Icons.Default.Groups)
    object DonationSuccess : Screen("donation_success", "Donation Recorded", Icons.Default.CheckCircle)
    object MembershipCard : Screen("membership_card", "Membership Card", Icons.Default.Badge)
    object CommunityWall : Screen("community_wall", "Community Wall", Icons.Default.Forum)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
    object Suspended : Screen("suspended", "Suspended", Icons.Default.Block)

    // Admin
    object AdminDashboard : Screen("admin_dashboard", "Admin Dashboard", Icons.Default.AdminPanelSettings)
    object MemberManagement : Screen("member_management", "Member Management", Icons.Default.People)
    object MemberDetails : Screen("member_details/{memberId}", "Member Details", Icons.Default.Person)
    object VolunteerHoursManagement : Screen("volunteer_hours_management", "Volunteer Hours", Icons.Default.Schedule)
}
