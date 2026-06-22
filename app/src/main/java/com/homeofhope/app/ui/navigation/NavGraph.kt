package com.homeofhope.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.homeofhope.app.ui.screens.about.AboutScreen
import com.homeofhope.app.ui.screens.blog.BlogDetailScreen
import com.homeofhope.app.ui.screens.blog.BlogScreen
import com.homeofhope.app.ui.screens.causes.CauseDetailScreen
import com.homeofhope.app.ui.screens.causes.CausesScreen
import com.homeofhope.app.ui.screens.contact.ContactScreen
import com.homeofhope.app.ui.screens.events.EventsScreen
import com.homeofhope.app.ui.screens.faq.FaqScreen
import com.homeofhope.app.ui.screens.help.HelpScreen
import com.homeofhope.app.ui.screens.home.HomeScreen
import com.homeofhope.app.ui.screens.legal.CookiesScreen
import com.homeofhope.app.ui.screens.legal.PrivacyScreen
import com.homeofhope.app.ui.screens.legal.TermsScreen
import com.homeofhope.app.ui.screens.member.LoginScreen
import com.homeofhope.app.ui.screens.member.MemberDashboardScreen
import com.homeofhope.app.ui.screens.member.MembershipCardScreen
import com.homeofhope.app.ui.screens.member.RegisterScreen
import com.homeofhope.app.ui.screens.member.SettingsScreen
import com.homeofhope.app.ui.screens.member.SuspendedScreen
import com.homeofhope.app.ui.screens.community.CommunityWallScreen
import com.homeofhope.app.ui.screens.services.ServicesScreen
import com.homeofhope.app.ui.screens.donation.DonationScreen
import com.homeofhope.app.ui.screens.donation.DonationSuccessScreen
import com.homeofhope.app.ui.viewmodel.ContactViewModel
import com.homeofhope.app.ui.viewmodel.DonationViewModel
import com.homeofhope.app.ui.viewmodel.MemberViewModel

import com.homeofhope.app.ui.screens.admin.*
import com.homeofhope.app.ui.viewmodel.AdminViewModel

@Composable
fun HomeOfHopeNavGraph(
    navController: NavHostController,
    contactViewModel: ContactViewModel,
    memberViewModel: MemberViewModel,
    donationViewModel: DonationViewModel,
    adminViewModel: AdminViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) { 
            HomeScreen(
                onContactClick = { navController.navigate(Screen.Contact.route) },
                onBecomeMemberClick = { navController.navigate(Screen.Register.route) },
                onViewAllCausesClick = { 
                    navController.navigate(Screen.Causes.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onViewAllEventsClick = { 
                    navController.navigate(Screen.Events.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(Screen.About.route) { AboutScreen() }
        composable(Screen.Causes.route) { 
            CausesScreen { causeId ->
                navController.navigate("cause_detail/$causeId")
            }
        }
        composable(Screen.CauseDetail.route) { backStackEntry ->
            val causeId = backStackEntry.arguments?.getString("causeId") ?: ""
            CauseDetailScreen(
                causeId = causeId,
                isLoggedIn = memberViewModel.currentUser != null,
                onDonateClick = {
                    navController.navigate(Screen.Donation.route)
                },
                onLoginClick = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }
        composable(Screen.Events.route) { 
            EventsScreen()
        }
        composable(Screen.Blog.route) { 
            BlogScreen { blogId ->
                navController.navigate("blog_detail/$blogId")
            }
        }
        composable(Screen.BlogDetail.route) { backStackEntry ->
            val blogId = backStackEntry.arguments?.getString("blogId") ?: ""
            BlogDetailScreen(
                blogId = blogId
            )
        }
        composable(Screen.Services.route) { ServicesScreen() }
        composable(Screen.Contact.route) { ContactScreen(viewModel = contactViewModel) }
        composable(Screen.FAQ.route) { FaqScreen() }
        composable(Screen.Help.route) { 
            HelpScreen(
                onContactClick = { navController.navigate(Screen.Contact.route) },
                onFaqClick = { navController.navigate(Screen.FAQ.route) }
            ) 
        }
        composable(Screen.Privacy.route) { PrivacyScreen() }
        composable(Screen.Terms.route) { TermsScreen() }
        composable(Screen.Cookies.route) { CookiesScreen() }

        // Membership
        composable(Screen.Register.route) {
            RegisterScreen(
                viewModel = memberViewModel,
                onRegisterSuccess = { navController.navigate(Screen.Login.route) },
                onBackClick = { navController.navigate(Screen.Login.route) }
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = memberViewModel,
                onLoginSuccess = { 
                    navController.navigate(Screen.MemberDashboard.route) {
                        popUpTo(Screen.Home.route)
                    }
                },
                onAdminLoginSuccess = {
                    navController.navigate(Screen.AdminDashboard.route) {
                        popUpTo(Screen.Home.route)
                    }
                },
                onRegisterClick = { navController.navigate(Screen.Register.route) },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Screen.MemberDashboard.route) {
            MemberDashboardScreen(
                viewModel = memberViewModel,
                donationViewModel = donationViewModel,
                onDonateClick = {
                    if (memberViewModel.currentUser != null) {
                        navController.navigate(Screen.Donation.route)
                    } else {
                        navController.navigate(Screen.Login.route)
                    }
                },
                onVolunteerClick = { navController.navigate(Screen.Events.route) },
                onEventsClick = { navController.navigate(Screen.Events.route) },
                onProfileClick = { navController.navigate(Screen.Settings.route) },
                onLogoutClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onMembershipCardClick = { navController.navigate(Screen.MembershipCard.route) },
                onCommunityWallClick = { navController.navigate(Screen.CommunityWall.route) },
                onSuspended = {
                    navController.navigate(Screen.Suspended.route) {
                        popUpTo(Screen.MemberDashboard.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.MembershipCard.route) {
            MembershipCardScreen(
                viewModel = memberViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.CommunityWall.route) {
            CommunityWallScreen(
                viewModel = memberViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                viewModel = memberViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Donation.route) {
            DonationScreen(
                viewModel = donationViewModel,
                memberViewModel = memberViewModel,
                onDonationSuccess = {
                    navController.navigate(Screen.DonationSuccess.route) {
                        popUpTo(Screen.Donation.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.DonationSuccess.route) {
            DonationSuccessScreen(
                onDoneClick = {
                    navController.navigate(Screen.MemberDashboard.route) {
                        popUpTo(Screen.MemberDashboard.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Suspended.route) {
            SuspendedScreen(
                onLogoutClick = {
                    memberViewModel.logout()
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onContactSupportClick = {
                    navController.navigate(Screen.Contact.route)
                }
            )
        }

        // Admin
        composable(Screen.AdminDashboard.route) {
            AdminDashboardScreen(
                viewModel = adminViewModel,
                onMemberManagementClick = { navController.navigate(Screen.MemberManagement.route) },
                onVolunteerHoursClick = { navController.navigate(Screen.VolunteerHoursManagement.route) },
                onLogoutClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.MemberManagement.route) {
            MemberManagementScreen(
                viewModel = adminViewModel,
                onMemberClick = { id -> navController.navigate("member_details/$id") },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Screen.MemberDetails.route) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getString("memberId")?.toIntOrNull() ?: 0
            MemberDetailsScreen(
                memberId = memberId,
                viewModel = adminViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Screen.VolunteerHoursManagement.route) {
            VolunteerHoursManagementScreen(
                viewModel = adminViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
