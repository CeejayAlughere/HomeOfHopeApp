package com.homeofhope.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.homeofhope.app.ui.screens.about.AboutScreen
import com.homeofhope.app.ui.screens.blog.BlogScreen
import com.homeofhope.app.ui.screens.causes.CauseDetailScreen
import com.homeofhope.app.ui.screens.causes.CausesScreen
import com.homeofhope.app.ui.screens.contact.ContactScreen
import com.homeofhope.app.ui.screens.donate.DonateScreen
import com.homeofhope.app.ui.screens.events.EventsScreen
import com.homeofhope.app.ui.screens.faq.FaqScreen
import com.homeofhope.app.ui.screens.help.HelpScreen
import com.homeofhope.app.ui.screens.home.HomeScreen
import com.homeofhope.app.ui.screens.legal.CookiesScreen
import com.homeofhope.app.ui.screens.legal.PrivacyScreen
import com.homeofhope.app.ui.screens.legal.TermsScreen
import com.homeofhope.app.ui.screens.services.ServicesScreen
import com.homeofhope.app.ui.screens.volunteer.VolunteerScreen
import com.homeofhope.app.ui.viewmodel.ContactViewModel
import com.homeofhope.app.ui.viewmodel.DonateViewModel
import com.homeofhope.app.ui.viewmodel.VolunteerViewModel

@Composable
fun HomeOfHopeNavGraph(
    navController: NavHostController,
    donateViewModel: DonateViewModel,
    volunteerViewModel: VolunteerViewModel,
    contactViewModel: ContactViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) { 
            HomeScreen(
                onDonateClick = { navController.navigate(Screen.Donate.route) },
                onVolunteerClick = { navController.navigate(Screen.Volunteer.route) },
                onContactClick = { navController.navigate(Screen.Contact.route) }
            ) {
                navController.navigate(Screen.Causes.route)
            }
        }
        composable(Screen.About.route) { AboutScreen() }
        composable(Screen.Causes.route) { 
            CausesScreen(onDonateClick = { navController.navigate(Screen.Donate.route) }) { causeId ->
                navController.navigate("cause_detail/$causeId")
            }
        }
        composable(Screen.CauseDetail.route) { backStackEntry ->
            val causeId = backStackEntry.arguments?.getString("causeId") ?: ""
            CauseDetailScreen(
                causeId = causeId,
                onBackClick = { navController.popBackStack() },
                onDonateClick = { navController.navigate(Screen.Donate.route) }
            )
        }
        composable(Screen.Events.route) { 
            EventsScreen(onVolunteerClick = { navController.navigate(Screen.Volunteer.route) }) 
        }
        composable(Screen.Blog.route) { BlogScreen() }
        composable(Screen.Services.route) { ServicesScreen() }
        composable(Screen.Donate.route) { DonateScreen(viewModel = donateViewModel) }
        composable(Screen.Volunteer.route) { VolunteerScreen(viewModel = volunteerViewModel) }
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
    }
}
