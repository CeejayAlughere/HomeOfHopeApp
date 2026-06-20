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
    onLogoutClick: () -> Unit
) {
    val user = viewModel?.currentUser
    val donations by if (user != null && donationViewModel != null) {
        donationViewModel.getDonationsForMember(user.id).collectAsState(initial = emptyList())
    } else {
        remember { mutableStateOf(emptyList<DonationRecord>()) }
    }
    
    val upcomingEvents by viewModel?.upcomingEvents?.collectAsState() ?: remember { mutableStateOf(emptyList()) }
    val joinedEvents by viewModel?.joinedEvents?.collectAsState() ?: remember { mutableStateOf(emptyList()) }
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
            // Premium Header Section
            HeaderSection(user?.fullName ?: "Member", donations.size, joinedEvents.size)

            Spacer(modifier = Modifier.height(24.dp))
            
            // Impact Summary Card
            ImpactSummaryCard(donations, joinedEvents)

            Spacer(modifier = Modifier.height(24.dp))
            
            // Membership Progress Bar
            MembershipProgressCard(donations.sumOf { it.amount })

            Spacer(modifier = Modifier.height(32.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(24.dp))

            // Dynamic Upcoming Events Section
            EventsSection(
                events = upcomingEvents,
                joinedEvents = joinedEvents,
                onJoinToggle = { eventId, isJoined -> 
                    viewModel?.toggleJoinEvent(eventId, isJoined)
                }
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
            AchievementsSection(donations.isNotEmpty(), joinedEvents.isNotEmpty())

            Spacer(modifier = Modifier.height(32.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(24.dp))

            // Recent Donations (Receipts) Section with Toggle
            DonationHistorySection(
                donations = donations,
                showAll = showAllReceipts,
                onToggleShowAll = { showAllReceipts = !showAllReceipts }
            )

            Spacer(modifier = Modifier.height(32.dp))
            HorizontalDivider()
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
fun ImpactSummaryCard(donations: List<DonationRecord>, joinedEvents: List<Event>) {
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
                    label = "Hours",
                    value = "${monthlyEventsCount * 4}",
                    icon = Icons.Default.Timer,
                    modifier = Modifier.weight(1f)
                )
                ImpactItem(
                    label = "Projects",
                    value = "${(monthlyDonations.size + monthlyEventsCount).coerceAtMost(5)}",
                    icon = Icons.Default.Public,
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
fun EventsSection(events: List<Event>, joinedEvents: List<Event>, onJoinToggle: (Int, Boolean) -> Unit) {
    Text(
        text = "📅 Upcoming Events",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(16.dp))
    
    if (events.isEmpty()) {
        EmptyStateCard("No upcoming events at the moment.")
    } else {
        events.forEach { event ->
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
        text = "📢 Latest Updates",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(16.dp))
    
    if (announcements.isEmpty()) {
        EmptyStateCard("Check back later for updates!")
    } else {
        announcements.forEach { announcement ->
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
        text = "🌟 Impact Stories",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(16.dp))
    
    if (stories.isEmpty()) {
        EmptyStateCard("Stories of hope coming soon.")
    } else {
        stories.forEach { story ->
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
fun MembershipProgressCard(totalDonated: Double) {
    val nextGoal = 1000.0
    val progress = (totalDonated / nextGoal).coerceIn(0.0, 1.0).toFloat()
    
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
                    text = "Road to Silver Supporter",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.labelLarge,
                    color = HopeGreen,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = HopeGreen,
                trackColor = HopeGreen.copy(alpha = 0.1f)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "R${totalDonated.toInt()} of R${nextGoal.toInt()} donated. Almost there!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun AchievementsSection(hasDonated: Boolean, hasVolunteered: Boolean) {
    Text(
        text = "🏆 My Badges",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(16.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BadgeItem(
            label = "First Donor",
            icon = Icons.Default.VolunteerActivism,
            isUnlocked = hasDonated,
            modifier = Modifier.weight(1f)
        )
        BadgeItem(
            label = "Hope Helper",
            icon = Icons.Default.Groups,
            isUnlocked = hasVolunteered,
            modifier = Modifier.weight(1f)
        )
        BadgeItem(
            label = "Community Star",
            icon = Icons.Default.Star,
            isUnlocked = false,
            modifier = Modifier.weight(1f)
        )
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
            text = "🧾 Giving History",
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
            onLogoutClick = {}
        )
    }
}
