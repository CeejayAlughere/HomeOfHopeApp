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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.homeofhope.app.data.local.entity.DonationRecord
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
    
    val joinedEvents by viewModel?.joinedEvents?.collectAsState() ?: remember { mutableStateOf(emptySet()) }
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
            HeaderSection(user?.fullName ?: "Member")

            Spacer(modifier = Modifier.height(24.dp))
            
            // Membership Progress Bar
            MembershipProgressCard(donations.sumOf { it.amount })

            Spacer(modifier = Modifier.height(32.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(24.dp))

            // My Impact Section
            Text(
                text = "📊 My Impact",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val totalDonated = donations.sumOf { it.amount }
                ImpactCard(Modifier.weight(1f), "R${String.format(Locale.getDefault(), "%.0f", totalDonated)}", "Total Donated", Icons.Default.Favorite)
                ImpactCard(Modifier.weight(1f), "${joinedEvents.size}", "Events Joined", Icons.Default.EventAvailable)
                ImpactCard(Modifier.weight(1f), "${joinedEvents.size * 4}", "Hours (est.)", Icons.Default.Timer)
            }

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
                text = "⚡ Quick Actions",
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
            HorizontalDivider()
            Spacer(modifier = Modifier.height(24.dp))

            // Featured Volunteer Opportunity
            FeaturedVolunteerCard(
                eventTitle = "Winter Food Drive",
                eventDate = "28 June 2026",
                isJoined = joinedEvents.contains("Winter Food Drive"),
                onJoinToggle = { viewModel?.toggleJoinEvent("Winter Food Drive") }
            )
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun HeaderSection(name: String) {
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
                text = "Welcome Back,",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Surface(
                    color = HopeGreen.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(100.dp)
                ) {
                    Text(
                        text = "Active Member",
                        color = HopeGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Joined June 2026",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
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
fun FeaturedVolunteerCard(eventTitle: String, eventDate: String, isJoined: Boolean, onJoinToggle: () -> Unit) {
    Text(
        text = "📅 Volunteer Opportunity",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(16.dp))
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isJoined) HopeGreen.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(40.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = HopeGreen.copy(alpha = 0.1f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Celebration, contentDescription = null, tint = HopeGreen)
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = eventTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = eventDate,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = onJoinToggle,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isJoined) Color.Gray else HopeGreen
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(if (isJoined) Icons.Default.Check else Icons.Default.Groups, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (isJoined) "Signed Up" else "Volunteer Now", fontWeight = FontWeight.Bold)
            }
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
fun ImpactCard(modifier: Modifier, value: String, label: String, icon: ImageVector) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = HopeGreen, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
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
