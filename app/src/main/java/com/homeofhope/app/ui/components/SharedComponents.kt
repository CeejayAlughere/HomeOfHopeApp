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
import androidx.compose.material.icons.filled.Email
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
fun NewsletterSection() {
    var email by remember { mutableStateOf("") }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = HopeLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null,
                tint = HopeGreen,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Subscribe to Our Newsletter",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Stay updated with our latest causes and events.",
                style = MaterialTheme.typography.bodyMedium,
                color = HopeGrey,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Enter your email") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { /* Handle subscription */ },
                    colors = ButtonDefaults.buttonColors(containerColor = HopeGreen),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text("Join")
                }
            }
        }
    }
}

@Composable
fun FooterSection() {
    val context = LocalContext.current
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(HopeDark)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Contact Details", color = HopeWhite, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        FooterItem(icon = Icons.Default.LocationOn, text = "35 Morocco St, Isivana, Thembisa, South Africa")
        FooterItem(icon = Icons.Default.Phone, text = "084 212 4937", onClick = { callPhone(context, "0842124937") })
        FooterItem(icon = Icons.Default.Email, text = "info@homeofhope.com", onClick = { sendEmail(context, "info@homeofhope.com") })
        
        Spacer(modifier = Modifier.height(16.dp))
        Text("© 2024 Siyazigabisa Home Of Hope. All Rights Reserved.", color = HopeWhite, fontSize = 12.sp)
        Text("Making a difference, one life at a time ♥", color = HopeGreen, fontSize = 12.sp)
    }
}

@Composable
fun FooterItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, onClick: (() -> Unit)? = null) {
    Surface(
        onClick = { onClick?.invoke() },
        color = Color.Transparent,
        enabled = onClick != null
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = HopeGreen, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, color = HopeWhite, fontSize = 14.sp)
        }
    }
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

fun callPhone(context: Context, number: String) {
    context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number")))
}

fun sendEmail(context: Context, address: String) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:$address")
    }
    context.startActivity(intent)
}
