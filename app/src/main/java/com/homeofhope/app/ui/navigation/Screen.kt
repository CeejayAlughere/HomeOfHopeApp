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
    object Donation : Screen("donation", "Donate Now", Icons.Default.VolunteerActivism)

    // Membership
    object Register : Screen("register", "Become a Member", Icons.Default.PersonAdd)
    object Login : Screen("login", "Login", Icons.AutoMirrored.Filled.Login)
    object MemberDashboard : Screen("member_dashboard", "Member Dashboard", Icons.Default.Dashboard)
    object MemberDonate : Screen("member_donate", "Donate", Icons.Default.VolunteerActivism)
    object MemberVolunteer : Screen("member_volunteer", "Volunteer", Icons.Default.Groups)
}
