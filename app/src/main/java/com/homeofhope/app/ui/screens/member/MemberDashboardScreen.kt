package com.homeofhope.app.ui.screens.member

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.homeofhope.app.data.local.entity.*
import com.homeofhope.app.data.model.Badge
import com.homeofhope.app.data.model.MembershipLevel
import com.homeofhope.app.ui.components.*
import com.homeofhope.app.ui.theme.*
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
    onCommunityWallClick: () -> Unit,
    onSuspended: () -> Unit
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
    val currentStatus by viewModel?.currentMemberStatus?.collectAsState() ?: remember { mutableStateOf("Active") }

    LaunchedEffect(currentStatus) {
        if (currentStatus == "Suspended") {
            onSuspended()
        }
    }

    LaunchedEffect(user?.id) {
        user?.let { donationViewModel?.refreshHistory(it.id) }
        viewModel?.refreshSharedProfile()
    }

    var showAllReceipts by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Member Hub",
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
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
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            HeaderSection(user?.fullName ?: "Member", donations.size, joinedEvents.size)
            TextButton(onClick = { user?.let { donationViewModel?.refreshHistory(it.id) } }, enabled = donationViewModel?.historyLoading != true) {
                Text(if (donationViewModel?.historyLoading == true) "Loading donations…" else "Refresh donation history")
            }
            donationViewModel?.historyError?.let { message ->
                Text(message, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(horizontal = 24.dp))
            }

            Text(
                text = "Your website and app donation records are shared. These records are not payment confirmation.",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            ImpactSummaryCard(donations, joinedEvents, totalHours)

            Spacer(modifier = Modifier.height(32.dp))

            QuickActionsSection(
                onDonateClick = onDonateClick,
                onVolunteerClick = onVolunteerClick,
                onMembershipCardClick = onMembershipCardClick,
                onCommunityWallClick = onCommunityWallClick
            )

            Spacer(modifier = Modifier.height(32.dp))
            
            MembershipProgressCard(membershipLevel, donations.sumOf { it.amount }, totalHours, joinedEvents.size)

            Spacer(modifier = Modifier.height(32.dp))

            EventsSection(
                events = upcomingEvents,
                joinedEvents = joinedEvents,
                onJoinToggle = { eventId, isJoined -> 
                    viewModel?.toggleJoinEvent(eventId, isJoined)
                },
                onViewAllClick = onEventsClick
            )

            Spacer(modifier = Modifier.height(32.dp))

            AnnouncementsSection(announcements)

            Spacer(modifier = Modifier.height(32.dp))

            ImpactStoriesSection(impactStories)

            Spacer(modifier = Modifier.height(32.dp))

            AchievementsSection(badges)

            Spacer(modifier = Modifier.height(32.dp))

            DonationHistorySection(
                donations = donations,
                showAll = showAllReceipts,
                onToggleShowAll = { showAllReceipts = !showAllReceipts }
            )

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
fun HeaderSection(name: String, totalDonations: Int, totalEvents: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primaryContainer)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(40.dp)
            )
        }
        Spacer(modifier = Modifier.width(20.dp))
        Column {
            Text(
                text = "Welcome Back,",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = if (totalDonations + totalEvents > 0) {
                    "You've made a difference today!"
                } else {
                    "Ready to make an impact?"
                },
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun ImpactSummaryCard(donations: List<DonationRecord>, joinedEvents: List<Event>, totalHours: Double) {
    HopeCard(
        modifier = Modifier.padding(horizontal = 16.dp),
        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
        elevation = 0.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "MY IMPACT SUMMARY",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ImpactItem(
                    label = "Donated",
                    value = "R${donations.sumOf { it.amount }.toInt()}",
                    icon = Icons.Default.Favorite,
                    modifier = Modifier.weight(1f)
                )
                ImpactItem(
                    label = "Events",
                    value = "${joinedEvents.size}",
                    icon = Icons.Default.Event,
                    modifier = Modifier.weight(1f)
                )
                ImpactItem(
                    label = "Hours",
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
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun QuickActionsSection(
    onDonateClick: () -> Unit,
    onVolunteerClick: () -> Unit,
    onMembershipCardClick: () -> Unit,
    onCommunityWallClick: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ActionCard(Modifier.weight(1f), "Donate", Icons.Default.AddCard, onDonateClick)
            ActionCard(Modifier.weight(1f), "Volunteer", Icons.Default.Handshake, onVolunteerClick)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ActionCard(Modifier.weight(1f), "ID Card", Icons.Default.Badge, onMembershipCardClick)
            ActionCard(Modifier.weight(1f), "Community", Icons.Default.Forum, onCommunityWallClick)
        }
    }
}

@Composable
fun ActionCard(modifier: Modifier, text: String, icon: ImageVector, onClick: () -> Unit) {
    HopeCard(
        modifier = modifier,
        onClick = onClick,
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun EventsSection(events: List<Event>, joinedEvents: List<Event>, onJoinToggle: (Int, Boolean) -> Unit, onViewAllClick: () -> Unit) {
    SectionHeader(
        subtitle = "Upcoming",
        title = "Events For You",
        actionText = "View All",
        onActionClick = onViewAllClick
    )
    
    if (events.isEmpty()) {
        EmptyState(
            title = "No Events",
            message = "Check back later for new opportunities.",
            icon = Icons.Default.EventBusy,
            modifier = Modifier.height(200.dp)
        )
    } else {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            events.distinctBy { it.title }.take(3).forEach { event ->
                val isJoined = joinedEvents.any { it.id == event.id }
                DashboardEventCard(event, isJoined) { onJoinToggle(event.id, isJoined) }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun DashboardEventCard(event: Event, isJoined: Boolean, onJoinClick: () -> Unit) {
    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val dateString = sdf.format(Date(event.date))

    HopeCard(
        containerColor = if (isJoined) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface,
        elevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = event.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "${event.location} • $dateString", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                if (isJoined) {
                    Icon(Icons.Default.CheckCircle, contentDescription = "Joined", tint = MaterialTheme.colorScheme.primary)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            HopeButton(
                text = if (isJoined) "Leave Event" else "Join Event",
                onClick = onJoinClick,
                containerColor = if (isJoined) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primary,
                contentColor = if (isJoined) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.height(44.dp)
            )
        }
    }
}

@Composable
fun AnnouncementsSection(announcements: List<Announcement>) {
    SectionHeader(subtitle = "Updates", title = "Community Wall")
    
    if (announcements.isEmpty()) {
        EmptyState(
            title = "All Quiet",
            message = "We'll post updates here soon.",
            icon = Icons.Default.NotificationsNone,
            modifier = Modifier.height(150.dp)
        )
    } else {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            announcements.distinctBy { it.title }.take(3).forEach { announcement ->
                AnnouncementItem(announcement)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun AnnouncementItem(announcement: Announcement) {
    val sdf = SimpleDateFormat("dd MMM", Locale.getDefault())
    val dateString = sdf.format(Date(announcement.date))

    HopeCard(elevation = 1.dp) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = dateString.split(" ")[0], fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                Text(text = dateString.split(" ")[1], style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = announcement.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text(
                    text = announcement.content,
                    style = MaterialTheme.typography.bodySmall,
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
    SectionHeader(subtitle = "Stories", title = "Hope in Action")
    
    if (stories.isEmpty()) {
        EmptyState(
            title = "No Stories",
            message = "Impact stories will appear here.",
            icon = Icons.Default.AutoStories,
            modifier = Modifier.height(150.dp)
        )
    } else {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(stories.distinctBy { it.title }) { story ->
                StoryCard(story)
            }
        }
    }
}

@Composable
fun StoryCard(story: ImpactStory) {
    HopeCard(
        modifier = Modifier.width(300.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        elevation = 2.dp
    ) {
        Column {
            if (story.imageUrl != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(story.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = story.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = story.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    minLines = 2
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date(story.date)),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
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
    
    HopeCard(
        modifier = Modifier.padding(horizontal = 16.dp),
        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        elevation = 0.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "MEMBERSHIP PROGRESS",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = currentLevel.label,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (nextLevel != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Next: ${nextLevel.label}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            if (nextLevel != null) {
                Spacer(modifier = Modifier.height(20.dp))
                
                val donationProgress = (totalDonated / nextLevel.minDonation).coerceIn(0.0, 1.0).toFloat()
                val hoursProgress = (totalHours / nextLevel.minHours).coerceIn(0.0, 1.0).toFloat()
                val eventsProgress = (eventsAttended.toDouble() / nextLevel.minEvents.toDouble()).coerceIn(0.0, 1.0).toFloat()
                
                val overallProgress = (donationProgress + hoursProgress + eventsProgress) / 3f
                
                LinearProgressIndicator(
                    progress = { overallProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(CircleShape),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ProgressDetail("Donations", totalDonated, nextLevel.minDonation, "R")
                    ProgressDetail("Volunteer Hours", totalHours, nextLevel.minHours, "")
                    ProgressDetail("Events Attended", eventsAttended.toDouble(), nextLevel.minEvents.toDouble(), "")
                }
            } else {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "You've reached the highest tier! 🏆",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ProgressDetail(label: String, current: Double, target: Double, prefix: String) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                text = "$prefix${current.toInt()} / $prefix${target.toInt()}",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        val progress = (current / target).coerceIn(0.0, 1.0).toFloat()
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth().height(4.dp).clip(CircleShape),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
        )
    }
}

@Composable
fun AchievementsSection(badges: List<Badge>) {
    SectionHeader(subtitle = "Rewards", title = "My Badges")
    
    if (badges.isEmpty()) {
        EmptyState(
            title = "No Badges Yet",
            message = "Complete actions to unlock achievements.",
            icon = Icons.Default.EmojiEvents,
            modifier = Modifier.height(150.dp)
        )
    } else {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(badges) { badge ->
                BadgeItem(
                    label = badge.label,
                    icon = badge.icon,
                    isUnlocked = badge.isUnlocked
                )
            }
        }
    }
}

@Composable
fun BadgeItem(label: String, icon: ImageVector, isUnlocked: Boolean) {
    val color = if (isUnlocked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Surface(
            modifier = Modifier.size(64.dp),
            shape = CircleShape,
            color = if (isUnlocked) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f),
            border = if (isUnlocked) androidx.compose.foundation.BorderStroke(2.dp, color) else androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.2f))
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(32.dp))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = if (isUnlocked) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun DonationHistorySection(donations: List<DonationRecord>, showAll: Boolean, onToggleShowAll: () -> Unit) {
    SectionHeader(
        subtitle = "History",
        title = "Recent Donations",
        actionText = if (donations.size > 3) (if (showAll) "Show Less" else "View All") else null,
        onActionClick = onToggleShowAll
    )
    
    if (donations.isEmpty()) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = "No donations yet. Start your journey today!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            val displayList = if (showAll) donations else donations.take(3)
            displayList.forEach { donation ->
                DonationReceiptItem(donation)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun DonationReceiptItem(donation: DonationRecord) {
    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val dateString = sdf.format(Date(donation.date))

    HopeCard(elevation = 1.dp) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.VolunteerActivism, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = "Donation", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Text(text = dateString, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Text(
                text = "R${String.format(Locale.getDefault(), "%.2f", donation.amount)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
