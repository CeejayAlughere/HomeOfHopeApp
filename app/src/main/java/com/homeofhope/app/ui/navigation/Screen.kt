package com.homeofhope.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.ContactMail
import androidx.compose.material.icons.filled.Cookie
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home      : Screen("home",      "Home",    Icons.Default.Home)
    object About     : Screen("about",     "About",   Icons.Default.Info)
    object Causes    : Screen("causes",    "Causes",  Icons.Default.Favorite)
    object Events    : Screen("events",    "Events",  Icons.Default.Event)
    object Blog      : Screen("blog",      "Blog",    Icons.Default.Article)
    object Services  : Screen("services",  "Services",Icons.Default.MedicalServices)
    object Donate    : Screen("donate",    "Donate",  Icons.Default.VolunteerActivism)
    object Volunteer : Screen("volunteer", "Volunteer",Icons.Default.People)
    object Contact   : Screen("contact",   "Contact", Icons.Default.ContactMail)
    object FAQ       : Screen("faq",       "FAQs",    Icons.Default.QuestionAnswer)
    object Help      : Screen("help",      "Help",    Icons.Default.Help)
    object Privacy   : Screen("privacy",   "Privacy", Icons.Default.PrivacyTip)
    object Terms     : Screen("terms",     "Terms",   Icons.Default.Gavel)
    object Cookies   : Screen("cookies",   "Cookies", Icons.Default.Cookie)
    object CauseDetail : Screen("cause_detail/{causeId}", "Cause Detail", Icons.Default.Favorite)
}
