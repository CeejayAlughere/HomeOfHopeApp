package com.homeofhope.app.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.homeofhope.app.ui.theme.HopeGreen
import com.homeofhope.app.ui.viewmodel.AdminViewModel

@Composable
fun AdminDashboardScreen(
    viewModel: AdminViewModel,
    onMemberManagementClick: () -> Unit,
    onVolunteerHoursClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val stats by viewModel.dashboardStats.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Welcome, Admin",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onLogoutClick) {
                Icon(Icons.Default.Logout, contentDescription = "Logout")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Statistics Summary",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(16.dp))

        val statItems = listOf(
            StatItem("Total Members", stats.totalMembers.toString(), Icons.Default.People),
            StatItem("Total Volunteers", stats.totalVolunteers.toString(), Icons.Default.Groups),
            StatItem("Donations", "R${stats.totalDonations}", Icons.Default.VolunteerActivism),
            StatItem("Volunteer Hours", stats.totalVolunteerHours.toString(), Icons.Default.Schedule),
            StatItem("Active Events", stats.activeEvents.toString(), Icons.Default.Event),
            StatItem("New Members", stats.newRegistrationsThisMonth.toString(), Icons.Default.PersonAdd)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(statItems) { item ->
                StatCard(item)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ActionCard(
                title = "Members",
                icon = Icons.Default.People,
                modifier = Modifier.weight(1f),
                onClick = onMemberManagementClick
            )
            ActionCard(
                title = "Hours",
                icon = Icons.Default.Schedule,
                modifier = Modifier.weight(1f),
                onClick = onVolunteerHoursClick
            )
        }
    }
}

data class StatItem(val title: String, val value: String, val icon: ImageVector)

@Composable
fun StatCard(item: StatItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(item.icon, contentDescription = null, tint = HopeGreen)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = item.value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(text = item.title, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun ActionCard(title: String, icon: ImageVector, modifier: Modifier = Modifier, onClick: () -> Unit) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, fontWeight = FontWeight.Medium)
        }
    }
}
