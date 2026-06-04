package com.homeofhope.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.homeofhope.app.data.network.RetrofitClient
import com.homeofhope.app.data.repository.HomeRepository
import com.homeofhope.app.ui.components.AppBottomNav
import com.homeofhope.app.ui.components.AppTopBar
import com.homeofhope.app.ui.navigation.HomeOfHopeNavGraph
import com.homeofhope.app.ui.navigation.Screen
import com.homeofhope.app.ui.theme.HomeOfHopeTheme
import com.homeofhope.app.ui.theme.HopeDark
import com.homeofhope.app.ui.theme.HopeGreen
import com.homeofhope.app.ui.theme.HopeWhite
import com.homeofhope.app.ui.viewmodel.ContactViewModel
import com.homeofhope.app.ui.viewmodel.DonateViewModel
import com.homeofhope.app.ui.viewmodel.VolunteerViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        
        val repository = HomeRepository(RetrofitClient.instance)
        val donateViewModel = DonateViewModel(repository)
        val volunteerViewModel = VolunteerViewModel(repository)
        val contactViewModel = ContactViewModel(repository)

        setContent {
            HomeOfHopeTheme {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val moreScreens = listOf(
                    Screen.Blog, Screen.Services, Screen.Donate, 
                    Screen.Volunteer, Screen.Contact, Screen.FAQ, 
                    Screen.Help, Screen.Privacy, Screen.Terms, Screen.Cookies
                )

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet(
                            modifier = Modifier.width(300.dp),
                            drawerContainerColor = HopeDark,
                            drawerContentColor = HopeWhite
                        ) {
                            Text("More Options", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge, color = HopeGreen)
                            HorizontalDivider(color = HopeWhite.copy(alpha = 0.1f))
                            moreScreens.forEach { screen ->
                                NavigationDrawerItem(
                                    label = { Text(screen.label) },
                                    selected = currentRoute == screen.route,
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                        scope.launch { drawerState.close() }
                                    },
                                    colors = NavigationDrawerItemDefaults.colors(
                                        unselectedContainerColor = HopeDark,
                                        selectedContainerColor = HopeGreen.copy(alpha = 0.2f),
                                        unselectedTextColor = HopeWhite,
                                        selectedTextColor = HopeGreen
                                    )
                                )
                            }
                        }
                    }
                ) {
                    Scaffold(
                        topBar = {
                            val title = when (currentRoute) {
                                Screen.Home.route -> "Home Of Hope"
                                Screen.About.route -> "About Us"
                                Screen.Causes.route -> "Our Causes"
                                Screen.Events.route -> "Upcoming Events"
                                Screen.Blog.route -> "Blog"
                                Screen.Services.route -> "Our Services"
                                Screen.Donate.route -> "Donate"
                                Screen.Volunteer.route -> "Volunteer"
                                Screen.Contact.route -> "Contact Us"
                                Screen.FAQ.route -> "FAQs"
                                Screen.Help.route -> "Help"
                                Screen.Privacy.route -> "Privacy Policy"
                                Screen.Terms.route -> "Terms of Use"
                                Screen.Cookies.route -> "Cookies Policy"
                                else -> "Home Of Hope"
                            }
                            AppTopBar(
                                title = title,
                                canNavigateBack = currentRoute != Screen.Home.route && 
                                                currentRoute != Screen.About.route && 
                                                currentRoute != Screen.Causes.route && 
                                                currentRoute != Screen.Events.route,
                                onBack = { navController.popBackStack() }
                            )
                        },
                        bottomBar = {
                            AppBottomNav(
                                navController = navController,
                                currentRoute = currentRoute,
                                onMoreClick = { scope.launch { drawerState.open() } }
                            )
                        }
                    ) { padding ->
                        HomeOfHopeNavGraph(
                            navController = navController,
                            donateViewModel = donateViewModel,
                            volunteerViewModel = volunteerViewModel,
                            contactViewModel = contactViewModel,
                            modifier = Modifier.padding(padding)
                        )
                    }
                }
            }
        }
    }
}
