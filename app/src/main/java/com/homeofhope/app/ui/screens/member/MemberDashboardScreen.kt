package com.homeofhope.app.ui.screens.member

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.homeofhope.app.data.local.entity.Announcement
import com.homeofhope.app.data.local.entity.DonationRecord
import com.homeofhope.app.data.local.entity.Event
import com.homeofhope.app.data.local.entity.ImpactStory
import com.homeofhope.app.data.local.entity.Member
import com.homeofhope.app.data.model.Badge
import com.homeofhope.app.data.model.MembershipLevel
import com.homeofhope.app.ui.theme.HomeOfHopeTheme
import com.homeofhope.app.ui.theme.HopeGreen
import com.homeofhope.app.ui.theme.HopeWhite
import com.homeofhope.app.ui.viewmodel.DonationViewModel
import com.homeofhope.app.ui.viewmodel.MemberViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberDashboardScreen(
    viewModel: MemberViewModel?,
    donationViewModel: DonationViewModel?,
    onDonateClick: () -> Unit,
    onVolunteerClick: () -> Unit,
    onEventsClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onMembershipCardClick: () -> Unit,
    onCommunityWallClick: () -> Unit
) {
    val user = viewModel?.currentUser
    val donations by if (user != null && donationViewModel != null) {
        donationViewModel.getDonationsForMember(user.id).collectAsState(initial = emptyList())
    } else {
        remember { mutableStateOf(emptyList<DonationRecord>()) }
    }
    
    val upcomingEvents by viewModel?.upcomingEvents?.collectAsState() ?: remember { mutableStateOf(emptyList()) }
    val joinedEvents by viewModel?.joinedEvents?.collectAsState() ?: remember { mutableStateOf(emptyList()) }
    val totalHours by viewModel?.totalHours?.collectAsState() ?: remember { mutableStateOf(0.0) }
    val membershipLevel by viewModel?.membershipLevel?.collectAsState() ?: remember { mutableStateOf(MembershipLevel.SEED_SUPPORTER) }
    val badges by viewModel?.badges?.collectAsState() ?: remember { mutableStateOf(emptyList<Badge>()) }
    val announcements by viewModel?.announcements?.collectAsState() ?: remember { mutableStateOf(emptyList()) }
    val impactStories by viewModel?.impactStories?.collectAsState() ?: remember { mutableStateOf(emptyList()) }

    var showAllReceipts by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Member Hub", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Profile Settings")
                    }
                    IconButton(onClick = {
                        viewModel?.logout()
                        onLogoutClick()
                    }) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            //  Header Section
            HeaderSection(user?.fullName ?: "Member", donations.size, joinedEvents.size)

            Spacer(modifier = Modifier.height(24.dp))

            // Quick Actions Section
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ActionButton(Modifier.weight(1f), "Donate", Icons.Default.AddCard, onDonateClick)
                ActionButton(Modifier.weight(1f), "Volunteer", Icons.Default.Handshake, onVolunteerClick)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ActionButton(Modifier.weight(1f), "Member Card", Icons.Default.Badge, onMembershipCardClick)
                ActionButton(Modifier.weight(1f), "Community", Icons.Default.Forum, onCommunityWallClick)
            }

            Spacer(modifier = Modifier.height(32.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(24.dp))
            
            // Impact Summary Card
            ImpactSummaryCard(donations, joinedEvents, totalHours)

            Spacer(modifier = Modifier.height(24.dp))
            
            // Membership Progress Bar
            MembershipProgressCard(membershipLevel, donations.sumOf { it.amount }, totalHours, joinedEvents.size)

            Spacer(modifier = Modifier.height(32.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(24.dp))

            // Dynamic Upcoming Events Section
            EventsSection(
                events = upcomingEvents,
                joinedEvents = joinedEvents,
                onJoinToggle = { eventId, isJoined -> 
                    viewModel?.toggleJoinEvent(eventId, isJoined)
                },
                onViewAllClick = onEventsClick
            )

            Spacer(modifier = Modifier.height(32.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(24.dp))

            // Announcements Section
            AnnouncementsSection(announcements)

            Spacer(modifier = Modifier.height(32.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(24.dp))

            // Impact Stories Section
            ImpactStoriesSection(impactStories)

            Spacer(modifier = Modifier.height(32.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(24.dp))

            // Achievements Badges
            AchievementsSection(badges)

            Spacer(modifier = Modifier.height(32.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(24.dp))

            // Recent Donations
            DonationHistorySection(
                donations = donations,
                showAll = showAllReceipts,
                onToggleShowAll = { showAllReceipts = !showAllReceipts }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun HeaderSection(name: String, totalDonations: Int, totalEvents: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Brush.linearGradient(listOf(HopeGreen, HopeGreen.copy(alpha = 0.6f)))),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                tint = HopeWhite,
                modifier = Modifier.size(36.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = "Welcome Back, $name 👋",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            val supportText = if (totalDonations + totalEvents > 0) {
                "You have supported ${totalDonations + totalEvents} community initiatives."
            } else {
                "Ready to make an impact today?"
            }
            Text(
                text = supportText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ImpactSummaryCard(donations: List<DonationRecord>, joinedEvents: List<Event>, totalHours: Double) {
    val calendar = Calendar.getInstance()
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentYear = calendar.get(Calendar.YEAR)

    val monthlyDonations = donations.filter {
        val d = Calendar.getInstance().apply { timeInMillis = it.date }
        d.get(Calendar.MONTH) == currentMonth && d.get(Calendar.YEAR) == currentYear
    }
    
    val monthlyEventsCount = joinedEvents.filter {
        val d = Calendar.getInstance().apply { timeInMillis = it.date }
        d.get(Calendar.MONTH) == currentMonth && d.get(Calendar.YEAR) == currentYear
    }.size

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "This Month's Impact",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ImpactItem(
                    label = "Donated",
                    value = "R${monthlyDonations.sumOf { it.amount }.toInt()}",
                    icon = Icons.Default.Favorite,
                    modifier = Modifier.weight(1f)
                )
                ImpactItem(
                    label = "Events",
                    value = "$monthlyEventsCount",
                    icon = Icons.Default.Event,
                    modifier = Modifier.weight(1f)
                )
                ImpactItem(
                    label = "Total Hours",
                    value = "${totalHours.toInt()}",
                    icon = Icons.Default.Timer,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun ImpactItem(label: String, value: String, icon: ImageVector, modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = null, tint = HopeGreen, modifier = Modifier.size(20.dp))
        Text(text = value, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(text = label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun EventsSection(events: List<Event>, joinedEvents: List<Event>, onJoinToggle: (Int, Boolean) -> Unit, onViewAllClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Upcoming Events",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        TextButton(onClick = onViewAllClick) {
            Text("View All", color = HopeGreen)
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
    
    if (events.isEmpty()) {
        EmptyStateCard("No upcoming events at the moment.")
    } else {
        // Only show first 3 unique events in dashboard
        events.distinctBy { it.title }.take(3).forEach { event ->
            val isJoined = joinedEvents.any { it.id == event.id }
            EventCard(event, isJoined) { onJoinToggle(event.id, isJoined) }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun EventCard(event: Event, isJoined: Boolean, onJoinClick: () -> Unit) {
    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val dateString = sdf.format(Date(event.date))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isJoined) HopeGreen.copy(alpha = 0.05f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = event.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, size = 12.dp, tint = Color.Gray)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "${event.location} • $dateString", fontSize = 12.sp, color = Color.Gray)
                    }
                }
                if (isJoined) {
                    Icon(Icons.Default.CheckCircle, contentDescription = "Joined", tint = HopeGreen)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = event.description,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onJoinClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isJoined) Color.Gray else HopeGreen
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(if (isJoined) "Leave Event" else "Join Event", fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun AnnouncementsSection(announcements: List<Announcement>) {
    Text(
        text = "Latest Community Updates",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(16.dp))
    
    if (announcements.isEmpty()) {
        EmptyStateCard("Check back later for updates!")
    } else {
        announcements.distinctBy { it.title }.forEach { announcement ->
            AnnouncementItem(announcement)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun AnnouncementItem(announcement: Announcement) {
    val sdf = SimpleDateFormat("dd MMM", Locale.getDefault())
    val dateString = sdf.format(Date(announcement.date))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier
                    .background(HopeGreen.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = dateString.split(" ")[0], fontWeight = FontWeight.Bold, fontSize = 14.sp, color = HopeGreen)
                Text(text = dateString.split(" ")[1], fontSize = 10.sp, color = HopeGreen)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = announcement.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(
                    text = announcement.content,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun ImpactStoriesSection(stories: List<ImpactStory>) {
    Text(
        text = "Impact Stories",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(16.dp))
    
    if (stories.isEmpty()) {
        EmptyStateCard("Stories of hope coming soon.")
    } else {
        stories.distinctBy { it.title }.forEach { story ->
            StoryCard(story)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun StoryCard(story: ImpactStory) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = story.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = story.description,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date(story.date)),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = HopeGreen
            )
        }
    }
}

@Composable
fun EmptyStateCard(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
    ) {
        Box(modifier = Modifier.padding(24.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(text = message, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
    }
}

@Composable
fun MembershipProgressCard(currentLevel: MembershipLevel, totalDonated: Double, totalHours: Double, eventsAttended: Int) {
    val nextLevel = when (currentLevel) {
        MembershipLevel.SEED_SUPPORTER -> MembershipLevel.HOPE_SUPPORTER
        MembershipLevel.HOPE_SUPPORTER -> MembershipLevel.SILVER_SUPPORTER
        MembershipLevel.SILVER_SUPPORTER -> MembershipLevel.GOLD_SUPPORTER
        MembershipLevel.GOLD_SUPPORTER -> MembershipLevel.LEGACY_SUPPORTER
        MembershipLevel.LEGACY_SUPPORTER -> null
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Current Tier: ${currentLevel.label}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                if (nextLevel != null) {
                    Text(
                        text = "Next: ${nextLevel.label}",
                        style = MaterialTheme.typography.labelMedium,
                        color = HopeGreen
                    )
                }
            }
            
            if (nextLevel != null) {
                Spacer(modifier = Modifier.height(16.dp))
                
                val donationProgress = (totalDonated / nextLevel.minDonation).coerceIn(0.0, 1.0).toFloat()
                val hoursProgress = (totalHours / nextLevel.minHours).coerceIn(0.0, 1.0).toFloat()
                val eventsProgress = (eventsAttended.toDouble() / nextLevel.minEvents.toDouble()).coerceIn(0.0, 1.0).toFloat()
                
                val overallProgress = (donationProgress + hoursProgress + eventsProgress) / 3f
                
                LinearProgressIndicator(
                    progress = { overallProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape),
                    color = HopeGreen,
                    trackColor = HopeGreen.copy(alpha = 0.1f)
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    ProgressDetail("Donation", totalDonated, nextLevel.minDonation, "R")
                    ProgressDetail("Hours", totalHours, nextLevel.minHours, "")
                    ProgressDetail("Events", eventsAttended.toDouble(), nextLevel.minEvents.toDouble(), "")
                }
            } else {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "You have reached the highest membership tier! Thank you for your incredible support.",
                    style = MaterialTheme.typography.bodySmall,
                    color = HopeGreen,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun ProgressDetail(label: String, current: Double, target: Double, prefix: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            text = "$prefix${current.toInt()} / $prefix${target.toInt()}",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun AchievementsSection(badges: List<Badge>) {
    Text(
        text = "My Badges",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(16.dp))
    
    if (badges.isEmpty()) {
        EmptyStateCard("Complete actions to unlock badges!")
    } else {
        // Display badges in a grid-like layout
        badges.chunked(3).forEach { rowBadges ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                rowBadges.forEach { badge ->
                    BadgeItem(
                        label = badge.label,
                        icon = badge.icon,
                        isUnlocked = badge.isUnlocked,
                        modifier = Modifier.weight(1f)
                    )
                }
                // Add spacers if the row is not full
                repeat(3 - rowBadges.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun BadgeItem(label: String, icon: ImageVector, isUnlocked: Boolean, modifier: Modifier) {
    val color = if (isUnlocked) HopeGreen else Color.Gray.copy(alpha = 0.3f)
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(64.dp),
            shape = CircleShape,
            color = color.copy(alpha = 0.1f),
            border = androidx.compose.foundation.BorderStroke(2.dp, color)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(32.dp))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = if (isUnlocked) MaterialTheme.colorScheme.onSurface else Color.Gray
        )
    }
}

@Composable
fun DonationHistorySection(donations: List<DonationRecord>, showAll: Boolean, onToggleShowAll: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Donation History",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        if (donations.size > 3) {
            TextButton(onClick = onToggleShowAll) {
                Text(if (showAll) "Show Less" else "View All", color = HopeGreen)
            }
        }
    }
    
    Spacer(modifier = Modifier.height(16.dp))
    
    if (donations.isEmpty()) {
        Text(
            text = "No donations yet. Start your journey today!",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(vertical = 16.dp)
        )
    } else {
        val displayList = if (showAll) donations else donations.take(3)
        displayList.forEach { donation ->
            DonationReceiptItem(donation)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun DonationReceiptItem(donation: DonationRecord) {
    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val dateString = sdf.format(Date(donation.date))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "Donation to Home of Hope", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(text = dateString, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(
                text = "R${String.format(Locale.getDefault(), "%.2f", donation.amount)}",
                fontWeight = FontWeight.ExtraBold,
                color = HopeGreen,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun ActionButton(modifier: Modifier, text: String, icon: ImageVector, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(12.dp),
        border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun Icon(icon: ImageVector, contentDescription: String?, size: androidx.compose.ui.unit.Dp, tint: Color) {
    androidx.compose.material3.Icon(icon, contentDescription = contentDescription, modifier = Modifier.size(size), tint = tint)
}

@Preview(showBackground = true)
@Composable
fun MemberDashboardPreview() {
    HomeOfHopeTheme {
        MemberDashboardScreen(
            viewModel = null,
            donationViewModel = null,
            onDonateClick = {},
            onVolunteerClick = {},
            onEventsClick = {},
            onProfileClick = {},
            onLogoutClick = {},
            onMembershipCardClick = {},
            onCommunityWallClick = {}
        )
    }
}
