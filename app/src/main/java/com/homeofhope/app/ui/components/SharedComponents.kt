package com.homeofhope.app.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.homeofhope.app.R
import com.homeofhope.app.ui.navigation.Screen
import com.homeofhope.app.ui.theme.*
import com.homeofhope.app.ui.theme.HopeDark
import com.homeofhope.app.ui.theme.HopeGreen
import com.homeofhope.app.ui.theme.HopeLight
import com.homeofhope.app.ui.theme.HopeWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(title: String, canNavigateBack: Boolean, onBack: () -> Unit) {
    CenterAlignedTopAppBar(
        title = { 
            Text(
                text = title, 
                color = HopeWhite,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            ) 
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = HopeWhite)
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = HopeDark,
            navigationIconContentColor = HopeWhite,
            titleContentColor = HopeWhite
        )
    )
}

@Composable
fun PageHeader(title: String, imageUrl: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.ic_logo),
            error = painterResource(R.drawable.ic_logo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                    )
                )
        )
        Text(
            text = title,
            color = HopeWhite,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp)
                .padding(bottom = 16.dp)
        )
    }
}

@Composable
fun SectionHeader(subtitle: String, title: String) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = subtitle, color = HopeGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(text = title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun AppBottomNav(navController: NavController, currentRoute: String?, onMoreClick: () -> Unit) {
    val items = listOf(
        Screen.Home,
        Screen.About,
        Screen.Causes,
        Screen.Events
    )
    
    Surface(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .fillMaxWidth()
            .height(72.dp),
        shape = RoundedCornerShape(36.dp),
        color = HopeDark,
        tonalElevation = 8.dp,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { screen ->
                val selected = currentRoute == screen.route
                BottomNavItem(
                    screenRoute = screen.route,
                    screenLabel = screen.label,
                    screenIcon = screen.icon,
                    selected = selected,
                    onClick = {
                        if (!selected) {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
            
            BottomNavItem(
                screenRoute = "more",
                screenLabel = "More",
                screenIcon = Icons.Default.Menu,
                selected = false,
                onClick = onMoreClick
            )
        }
    }
}

@Composable
fun BottomNavItem(screenRoute: String, screenLabel: String, screenIcon: androidx.compose.ui.graphics.vector.ImageVector, selected: Boolean, onClick: () -> Unit) {
    val contentColor = if (selected) HopeGreen else HopeWhite.copy(alpha = 0.6f)
    val containerColor = if (selected) HopeGreen.copy(alpha = 0.15f) else Color.Transparent
    
    Box(
        modifier = Modifier
            .size(width = 64.dp, height = 48.dp)
            .background(containerColor, RoundedCornerShape(24.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = screenIcon,
                contentDescription = screenLabel,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
            if (selected) {
                Text(
                    text = screenLabel,
                    color = contentColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

fun openUrl(context: Context, url: String) {
    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
}
