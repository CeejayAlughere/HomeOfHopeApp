package com.homeofhope.app.ui.screens.admin

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.homeofhope.app.ui.components.AppTopBar
import com.homeofhope.app.ui.components.HopeCard
import com.homeofhope.app.ui.components.SectionHeader
import com.homeofhope.app.ui.theme.HopeGreen
import com.homeofhope.app.ui.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    viewModel: AdminViewModel,
    onMemberManagementClick: () -> Unit,
    onVolunteerHoursClick: () -> Unit,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val stats by viewModel.dashboardStats.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Admin Portal",
                canNavigateBack = true,
                onBack = onBackClick,
                actions = {
                    IconButton(onClick = onLogoutClick) {
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
            
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "System Overview",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Welcome back, Administrator.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Main Stats Grid
            val statItems = listOf(
                StatItem("Members", stats.totalMembers.toString(), Icons.Default.People, MaterialTheme.colorScheme.primary),
                StatItem("Donations", "R${stats.totalDonations}", Icons.Default.VolunteerActivism, MaterialTheme.colorScheme.secondary),
                StatItem("Volunteers", stats.totalVolunteers.toString(), Icons.Default.Groups, MaterialTheme.colorScheme.tertiary),
                StatItem("Hours", stats.totalVolunteerHours.toString(), Icons.Default.Schedule, MaterialTheme.colorScheme.error)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .height(300.dp)
                    .padding(horizontal = 16.dp)
            ) {
                items(statItems) { item ->
                    StatCard(item)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Growth Chart Card
            GrowthChartCard(stats.newRegistrationsThisMonth)

            Spacer(modifier = Modifier.height(32.dp))

            SectionHeader(subtitle = "Management", title = "Quick Actions")
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AdminActionCard(
                    title = "Manage Members",
                    description = "View, edit and audit accounts",
                    icon = Icons.Default.People,
                    modifier = Modifier.weight(1f),
                    onClick = onMemberManagementClick
                )
                AdminActionCard(
                    title = "Log Hours",
                    description = "Verify volunteer services",
                    icon = Icons.Default.Schedule,
                    modifier = Modifier.weight(1f),
                    onClick = onVolunteerHoursClick
                )
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

data class StatItem(val title: String, val value: String, val icon: ImageVector, val color: Color)

@Composable
fun StatCard(item: StatItem) {
    HopeCard(
        containerColor = item.color.copy(alpha = 0.1f),
        elevation = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(item.color.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(item.icon, contentDescription = null, tint = item.color, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = item.value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = item.color
            )
            Text(
                text = item.title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun GrowthChartCard(newMembers: Int) {
    HopeCard(
        modifier = Modifier.padding(horizontal = 16.dp),
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "MONTHLY GROWTH",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "+$newMembers New Members",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Reflects registrations in the current calendar month.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Box(modifier = Modifier.size(80.dp), contentAlignment = Alignment.Center) {
                val primaryColor = MaterialTheme.colorScheme.primary
                val secondaryColor = MaterialTheme.colorScheme.outlineVariant
                
                Canvas(modifier = Modifier.size(60.dp)) {
                    drawArc(
                        color = secondaryColor,
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                    )
                    drawArc(
                        color = primaryColor,
                        startAngle = -90f,
                        sweepAngle = 240f, // Example static value for "growth"
                        useCenter = false,
                        style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
                Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, tint = primaryColor)
            }
        }
    }
}

@Composable
fun AdminActionCard(
    title: String,
    description: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    HopeCard(
        onClick = onClick,
        modifier = modifier,
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 14.sp
            )
        }
    }
}
