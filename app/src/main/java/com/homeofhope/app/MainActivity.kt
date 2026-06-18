package com.homeofhope.app

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.homeofhope.app.data.local.AppDatabase
import com.homeofhope.app.data.network.RetrofitClient
import com.homeofhope.app.data.repository.HomeRepository
import com.homeofhope.app.data.repository.MemberRepository
import com.homeofhope.app.ui.components.AppBottomNav
import com.homeofhope.app.ui.components.AppTopBar
import com.homeofhope.app.ui.navigation.HomeOfHopeNavGraph
import com.homeofhope.app.ui.navigation.Screen
import com.homeofhope.app.ui.theme.HomeOfHopeTheme
import com.homeofhope.app.ui.theme.HopeDark
import com.homeofhope.app.ui.theme.HopeGreen
import com.homeofhope.app.ui.theme.HopeWhite
import com.homeofhope.app.ui.viewmodel.ContactViewModel
import com.homeofhope.app.ui.viewmodel.DonationViewModel
import com.homeofhope.app.ui.viewmodel.MemberViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        
        // Add a custom exit animation for the splash screen
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            // Create a fade out and scale up animation
            val fadeOut = ObjectAnimator.ofFloat(
                splashScreenView.view,
                View.ALPHA,
                1f,
                0f
            )
            fadeOut.interpolator = AnticipateInterpolator()
            fadeOut.duration = 500L

            val scaleX = ObjectAnimator.ofFloat(
                splashScreenView.iconView,
                View.SCALE_X,
                1f,
                1.5f
            )
            scaleX.interpolator = AnticipateInterpolator()
            scaleX.duration = 500L

            val scaleY = ObjectAnimator.ofFloat(
                splashScreenView.iconView,
                View.SCALE_Y,
                1f,
                1.5f
            )
            scaleY.interpolator = AnticipateInterpolator()
            scaleY.duration = 500L

            // Run animations together and remove splash screen view when finished
            fadeOut.doOnEnd { splashScreenView.remove() }
            
            fadeOut.start()
            scaleX.start()
            scaleY.start()
        }
        
        val repository = HomeRepository(RetrofitClient.instance)
        val contactViewModel = ContactViewModel(repository)
        val donationViewModel = DonationViewModel(repository)

        // Room Database Setup
        val database = AppDatabase.getDatabase(this)
        val memberRepository = MemberRepository(database.memberDao())
        val memberViewModel = MemberViewModel(memberRepository)

        setContent {
            HomeOfHopeTheme {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val moreScreens = listOf(
                    Screen.Donation, Screen.Blog, Screen.Services, Screen.Contact, 
                    Screen.FAQ, Screen.Help, Screen.Privacy, Screen.Terms, Screen.Cookies
                )

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet(
                            modifier = Modifier.width(300.dp),
                            drawerContainerColor = HopeDark,
                            drawerContentColor = HopeWhite
                        ) {
                            Text(
                                text = "Navigation Drawer", 
                                modifier = Modifier.padding(24.dp), 
                                style = MaterialTheme.typography.headlineSmall, 
                                fontWeight = FontWeight.Bold,
                                color = HopeGreen
                            )
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 24.dp),
                                color = HopeWhite.copy(alpha = 0.1f)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            moreScreens.forEach { screen ->
                                NavigationDrawerItem(
                                    label = { Text(screen.label, fontWeight = FontWeight.Medium) },
                                    icon = { Icon(screen.icon, contentDescription = null) },
                                    selected = currentRoute == screen.route,
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                        scope.launch { drawerState.close() }
                                    },
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = NavigationDrawerItemDefaults.colors(
                                        unselectedContainerColor = Color.Transparent,
                                        selectedContainerColor = HopeGreen.copy(alpha = 0.2f),
                                        unselectedTextColor = HopeWhite.copy(alpha = 0.7f),
                                        selectedTextColor = HopeGreen,
                                        unselectedIconColor = HopeWhite.copy(alpha = 0.7f),
                                        selectedIconColor = HopeGreen
                                    )
                                )
                            }
                        }
                    }
                ) {
                    Scaffold(
                        topBar = {
                            val title = when {
                                currentRoute == Screen.Home.route -> "Home Of Hope"
                                currentRoute == Screen.About.route -> "About Us"
                                currentRoute == Screen.Causes.route -> "Our Causes"
                                currentRoute == Screen.Events.route -> "Upcoming Events"
                                currentRoute == Screen.Blog.route -> "Blog"
                                currentRoute == Screen.Services.route -> "Our Services"
                                currentRoute == Screen.Contact.route -> "Contact Us"
                                currentRoute == Screen.FAQ.route -> "FAQs"
                                currentRoute == Screen.Help.route -> "Help"
                                currentRoute == Screen.Privacy.route -> "Privacy Policy"
                                currentRoute == Screen.Terms.route -> "Terms of Use"
                                currentRoute == Screen.Cookies.route -> "Cookies Policy"
                                currentRoute == Screen.Register.route -> "Become a Member"
                                currentRoute == Screen.Login.route -> "Login"
                                currentRoute == Screen.MemberDashboard.route -> "Member Dashboard"
                                currentRoute == Screen.Donation.route -> "Make a Donation"
                                currentRoute?.startsWith("cause_detail") == true -> "Cause Details"
                                currentRoute?.startsWith("blog_detail") == true -> "Article"
                                else -> "Home Of Hope"
                            }
                            val canNavigateBack = currentRoute != Screen.Home.route && currentRoute != null
                            
                            AppTopBar(
                                title = title,
                                canNavigateBack = canNavigateBack,
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
                            contactViewModel = contactViewModel,
                            memberViewModel = memberViewModel,
                            donationViewModel = donationViewModel,
                            modifier = Modifier.padding(padding)
                        )
                    }
                }
            }
        }
    }
}
