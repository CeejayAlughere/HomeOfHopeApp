package com.homeofhope.app.ui.screens.events

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.homeofhope.app.ui.components.FooterSection
import com.homeofhope.app.ui.components.NewsletterSection
import com.homeofhope.app.ui.components.PageHeader
import com.homeofhope.app.ui.theme.HopeGreen
import com.homeofhope.app.ui.theme.HopeWhite

@Composable
fun EventsScreen(onVolunteerClick: () -> Unit) {
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
                EventCard(event, onVolunteerClick)
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
        
        NewsletterSection()
        FooterSection()
    }
}

@Composable
fun EventCard(event: Event, onVolunteerClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            AsyncImage(
                model = event.imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(180.dp),
                contentScale = ContentScale.Crop
            )
            
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = event.title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    EventMetaItem(Icons.Default.CalendarToday, event.date)
                    EventMetaItem(Icons.Default.Schedule, event.time)
                }
                Spacer(modifier = Modifier.height(8.dp))
                EventMetaItem(Icons.Default.LocationOn, event.location)
                
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = event.description, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = onVolunteerClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = HopeGreen),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
                ) {
                    Text("Join Now", color = HopeWhite)
                }
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
