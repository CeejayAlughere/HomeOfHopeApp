package com.homeofhope.app

import android.animation.ObjectAnimator
import androidx.lifecycle.lifecycleScope
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
import com.homeofhope.app.data.local.entity.Announcement
import com.homeofhope.app.data.local.entity.Event
import com.homeofhope.app.data.local.entity.ImpactStory
import com.homeofhope.app.data.network.RetrofitClient
import com.homeofhope.app.data.repository.*
import com.homeofhope.app.ui.components.AppBottomNav
import com.homeofhope.app.ui.components.AppTopBar
import com.homeofhope.app.ui.navigation.HomeOfHopeNavGraph
import com.homeofhope.app.ui.navigation.Screen
import com.homeofhope.app.ui.theme.HomeOfHopeTheme
import com.homeofhope.app.ui.theme.HopeDark
import com.homeofhope.app.ui.theme.HopeGreen
import com.homeofhope.app.ui.theme.HopeWhite
import com.homeofhope.app.ui.viewmodel.*
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
        
        // Room Database Setup
        val database = AppDatabase.getDatabase(this)
        val donationRepository = DonationRepository(database.donationDao())
        val donationViewModel = DonationViewModel(repository, donationRepository)

        val memberRepository = MemberRepository(database.memberDao())
        val eventRepository = EventRepository(database.eventDao())
        val communityRepository = CommunityRepository(database.communityDao())
        val announcementRepository = AnnouncementRepository(database.announcementDao())
        val impactStoryRepository = ImpactStoryRepository(database.impactStoryDao())
        
        val adminRepository = AdminRepository(
            database.adminDao(),
            database.memberDao(),
            database.volunteerHoursDao(),
            database.donationDao(),
            database.eventDao()
        )
        val adminViewModel = AdminViewModel(adminRepository)

        val memberViewModel = MemberViewModel(
            memberRepository,
            eventRepository,
            donationRepository,
            communityRepository,
            announcementRepository,
            impactStoryRepository,
            adminRepository
        )

        // Seed data for development/debug mode
        seedData(adminRepository, eventRepository, announcementRepository, impactStoryRepository, lifecycleScope)

        setContent {
            HomeOfHopeTheme {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val moreScreens = listOf(
                    Screen.Blog, Screen.Services, Screen.Contact,
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
                            adminViewModel = adminViewModel,
                            modifier = Modifier.padding(padding)
                        )
                    }
                }
            }
        }
    }

    private fun seedData(
        adminRepo: AdminRepository,
        eventRepo: EventRepository,
        announcementRepo: AnnouncementRepository,
        impactStoryRepo: ImpactStoryRepository,
        scope: androidx.lifecycle.LifecycleCoroutineScope
    ) {
        scope.launch {
            // Seed Admin
            if (adminRepo.getAdminByEmail("admin@hoh.org") == null) {
                adminRepo.insertAdmin(
                    com.homeofhope.app.data.local.entity.Admin(
                        email = "admin@hoh.org",
                        password = "Admin@123"
                    )
                )
            }
            // Seed Events
            if (!eventRepo.eventExists("Winter Food Drive")) {
                eventRepo.insertEvent(
                    Event(
                        title = "Winter Food Drive",
                        date = System.currentTimeMillis() + 86400000 * 7,
                        location = "Community Center",
                        description = "Join us in collecting and distributing food to those in need this winter."
                    )
                )
            }
            if (!eventRepo.eventExists("Youth Mentorship Program")) {
                eventRepo.insertEvent(
                    Event(
                        title = "Youth Mentorship Program",
                        date = System.currentTimeMillis() + 86400000 * 14,
                        location = "Local Library",
                        description = "Help guide the next generation. Share your skills and experience."
                    )
                )
            }

            // Seed Announcements
            if (!announcementRepo.announcementExists("New Partnership Announced")) {
                announcementRepo.insertAnnouncement(
                    Announcement(
                        title = "New Partnership Announced",
                        content = "We are excited to partner with Local Bank to expand our reach.",
                        date = System.currentTimeMillis()
                    )
                )
            }
            if (!announcementRepo.announcementExists("Annual Gala Success")) {
                announcementRepo.insertAnnouncement(
                    Announcement(
                        title = "Annual Gala Success",
                        content = "Thank you to everyone who attended! We raised over R100k.",
                        date = System.currentTimeMillis() - 86400000 * 2
                    )
                )
            }

            // Seed Impact Stories
            if (!impactStoryRepo.storyExists("Education for All")) {
                impactStoryRepo.insertImpactStory(
                    ImpactStory(
                        title = "Education for All",
                        description = "Thanks to your donations, 50 more children started school this month.",
                        date = System.currentTimeMillis() - 86400000 * 30
                    )
                )
            }
            if (!impactStoryRepo.storyExists("Clean Water Initiative")) {
                impactStoryRepo.insertImpactStory(
                    ImpactStory(
                        title = "Clean Water Initiative",
                        description = "Three new wells were completed in the northern district.",
                        date = System.currentTimeMillis() - 86400000 * 60
                    )
                )
            }
        }
    }
}
