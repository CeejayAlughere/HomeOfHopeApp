package com.homeofhope.app.ui.screens.events

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.homeofhope.app.R
import com.homeofhope.app.ui.components.PageHeader
import com.homeofhope.app.ui.theme.HopeDark
import com.homeofhope.app.ui.theme.HopeGreen
import com.homeofhope.app.ui.theme.HopeWhite

@Composable
fun EventsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        PageHeader(title = "Upcoming Events", imageUrl = "https://images.unsplash.com/photo-1511632765486-a01980e01a18?q=80&w=2070")
        
        Column(modifier = Modifier.padding(16.dp)) {
            val events = listOf(
                Event("Community Food Drive", "21 July 2026", "08:00 – 10:00", "Thembisa", "Join us as we collect and distribute food parcels to vulnerable families in Thembisa. Every donation of non-perishable food items makes a real difference.", "https://images.unsplash.com/photo-1593113598332-cd288d649433?q=80&w=2070"),
                Event("New Year Outreach Programme", "01 January 2026", "09:00 – 12:00", "Thembisa", "Start the new year by giving back. We will be visiting local shelters and distributing care packages to individuals and families starting the year in hardship.", "https://images.unsplash.com/photo-1469571486292-0ba58a3f068b?q=80&w=2070"),
                Event("Youth Skills & Empowerment Day", "01 April 2026", "11:00 – 15:00", "Thembisa", "A full afternoon of workshops, mentorship sessions and career guidance designed to equip the youth of Thembisa with practical skills for a better future.", "https://images.unsplash.com/photo-1529070538774-1843cb3265df?q=80&w=2070"),
                Event("Winter Blanket Drive", "04 May 2026", "10:00 – 14:00", "Thembisa", "As winter approaches, many of our community members are without warm clothing and blankets. Join us to help distribute warmth to those who need it most.", "https://images.unsplash.com/photo-1542810634-71277d95dcbb?q=80&w=2070")
            )
            
            events.forEach { event ->
                EventCard(event)
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun EventCard(event: Event) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(modifier = Modifier.height(200.dp)) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(event.imageUrl)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.ic_logo),
                    error = painterResource(R.drawable.ic_logo),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                // Date Badge
                Surface(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopEnd),
                    color = HopeGreen,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val dateParts = event.date.split(" ")
                        if (dateParts.size >= 2) {
                            Text(text = dateParts[0], color = HopeWhite, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                            Text(text = dateParts[1].substring(0, 3).uppercase(), color = HopeWhite, style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
            
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    EventMetaItem(Icons.Default.Schedule, event.time)
                    EventMetaItem(Icons.Default.LocationOn, event.location)
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = event.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 22.sp
                )
            }
        }
    }
}

@Composable
fun EventMetaItem(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, tint = HopeGreen, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

data class Event(val title: String, val date: String, val time: String, val location: String, val description: String, val imageUrl: String)
