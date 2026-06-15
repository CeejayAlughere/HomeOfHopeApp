package com.homeofhope.app.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
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
import com.homeofhope.app.ui.theme.HopeDark
import com.homeofhope.app.ui.theme.HopeGreen
import com.homeofhope.app.ui.theme.HopeLight
import com.homeofhope.app.ui.theme.HopeWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(title: String, canNavigateBack: Boolean, onBack: () -> Unit) {
    TopAppBar(
        title = { Text(text = title, color = HopeWhite) },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = HopeWhite)
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = HopeDark)
    )
}

@Composable
fun NewsletterSection() {
    var email by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(HopeLight)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Subscribe to Our Monthly Newsletter",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Enter your email") },
                singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { /* Handle subscription */ },
                colors = ButtonDefaults.buttonColors(containerColor = HopeGreen),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text("Subscribe")
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
            .height(200.dp)
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
                .background(Brush.verticalGradient(listOf(Color.Transparent, HopeDark.copy(alpha = 0.7f))))
        )
        Text(
            text = title,
            color = HopeWhite,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp)
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
    
    NavigationBar(containerColor = HopeDark) {
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                label = { Text(screen.label, fontSize = 10.sp) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = HopeGreen,
                    selectedTextColor = HopeGreen,
                    unselectedIconColor = HopeWhite.copy(alpha = 0.6f),
                    unselectedTextColor = HopeWhite.copy(alpha = 0.6f),
                    indicatorColor = Color.Transparent
                )
            )
        }
        
        NavigationBarItem(
            icon = { Icon(Icons.Default.Menu, contentDescription = "More") },
            label = { Text("More", fontSize = 10.sp) },
            selected = false,
            onClick = onMoreClick,
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = HopeWhite.copy(alpha = 0.6f),
                unselectedTextColor = HopeWhite.copy(alpha = 0.6f),
                indicatorColor = Color.Transparent
            )
        )
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
