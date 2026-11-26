package com.homeofhope.app.ui.screens.member

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.homeofhope.app.ui.theme.HopeDark
import com.homeofhope.app.ui.theme.HopeGreen
import com.homeofhope.app.ui.theme.HopeWhite
import com.homeofhope.app.ui.components.AppTopBar
import com.homeofhope.app.ui.viewmodel.MemberViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembershipCardScreen(
    viewModel: MemberViewModel,
    onBackClick: () -> Unit
) {
    val user = viewModel.currentUser
    if (user == null) {
        Column {
            AppTopBar(title = "Digital Member Card", canNavigateBack = true, onBack = onBackClick)
            Text("Sign in to view your profile.", modifier = Modifier.padding(16.dp))
        }
        return
    }
    val level by viewModel.membershipLevel.collectAsState()
    val sdf = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
    val joinDate = sdf.format(Date(user.joinDate))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Digital Member Card", color = HopeWhite) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = HopeWhite)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = HopeDark)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                listOf(HopeDark, HopeGreen)
                            )
                        )
                        .padding(24.dp)
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Home Of Hope",
                                color = HopeWhite,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp
                            )
                            Surface(
                                color = HopeWhite.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = level.label.uppercase(),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    color = HopeWhite,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.weight(1f))
                        
                        Text(
                            text = user.fullName,
                            color = HopeWhite,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Member ID: ${user.memberNumber}",
                            color = HopeWhite.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Column {
                                Text(
                                    text = "JOIN DATE",
                                    color = HopeWhite.copy(alpha = 0.5f),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = joinDate,
                                    color = HopeWhite,
                                    fontSize = 14.sp
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.QrCode2,
                                contentDescription = null,
                                tint = HopeWhite,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            Text(
                text = "Show this card at events to check in and record your volunteer hours.",
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
