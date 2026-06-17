package com.homeofhope.app.ui.screens.member

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.homeofhope.app.ui.viewmodel.MemberViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberDashboardScreen(
    viewModel: MemberViewModel,
    onDonateClick: () -> Unit,
    onVolunteerClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val user = viewModel.currentUser

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Member Dashboard") },
                actions = {
                    IconButton(onClick = {
                        viewModel.logout()
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Member Status: ACTIVE",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Welcome, ${user?.fullName ?: "Member"}",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "What would you like to do today?",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(24.dp))

            DashboardButton(
                text = "Donate to a Cause",
                icon = Icons.Default.Favorite,
                onClick = onDonateClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            DashboardButton(
                text = "Volunteer for an Event",
                icon = Icons.Default.Handshake,
                onClick = onVolunteerClick
            )
        }
    }
}

@Composable
fun DashboardButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(icon, contentDescription = null)
            Text(text = text, fontSize = 16.sp)
        }
    }
}
