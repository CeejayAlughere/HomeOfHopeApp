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
import com.homeofhope.app.ui.screens.member.RegisterScreen
import com.homeofhope.app.ui.screens.services.ServicesScreen
import com.homeofhope.app.ui.screens.donation.DonationScreen
import com.homeofhope.app.ui.screens.donation.DonationSuccessScreen
import com.homeofhope.app.ui.viewmodel.ContactViewModel
import com.homeofhope.app.ui.viewmodel.DonationViewModel
import com.homeofhope.app.ui.viewmodel.MemberViewModel

@Composable
fun HomeOfHopeNavGraph(
    navController: NavHostController,
    contactViewModel: ContactViewModel,
    memberViewModel: MemberViewModel,
    donationViewModel: DonationViewModel,
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
                onDonateClick = { navController.navigate(Screen.Donation.route) }
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
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Screen.MemberDashboard.route) {
            MemberDashboardScreen(
                viewModel = memberViewModel,
                donationViewModel = donationViewModel,
                onDonateClick = { navController.navigate(Screen.Donation.route) },
                onVolunteerClick = { navController.navigate(Screen.Events.route) },
                onEventsClick = { navController.navigate(Screen.Events.route) },
                onProfileClick = { /* Navigate to profile if it exists */ },
                onLogoutClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
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
    }
}
