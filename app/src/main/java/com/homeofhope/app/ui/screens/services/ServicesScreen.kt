package com.homeofhope.app.ui.screens.services

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.homeofhope.app.ui.components.PageHeader
import com.homeofhope.app.ui.theme.HopeGreen

@Composable
fun ServicesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        PageHeader(title = "Our Services", imageUrl = "https://images.unsplash.com/photo-1469571486292-0ba58a3f068b?q=80&w=2070")
        
        Column(modifier = Modifier.padding(16.dp)) {
            val services = listOf(
                ServiceItem(Icons.Default.Restaurant, "Healthy Food", "We provide nutritious daily meals to vulnerable individuals and families, ensuring that no one in our community goes to bed hungry."),
                ServiceItem(Icons.Default.WaterDrop, "Pure Water", "Access to clean, safe drinking water is a basic human right. We work to deliver reliable water solutions to underserved communities."),
                ServiceItem(Icons.Default.MedicalServices, "Health Care", "Our health programmes connect people with essential medical support, wellness check-ups and mental health resources at no cost to them."),
                ServiceItem(Icons.Default.School, "Primary Education", "We believe every child deserves a quality education. We fund school supplies, tutoring and learning programmes for children from disadvantaged backgrounds."),
                ServiceItem(Icons.Default.Home, "Residence Facilities", "Home Of Hope provides safe, temporary shelter for individuals and families facing homelessness, offering them a secure place to rebuild their lives."),
                ServiceItem(Icons.Default.VolunteerActivism, "Social Care", "Our dedicated social workers and volunteers offer counselling, family support and community outreach programmes to help people overcome hardship and find stability.")
            )
            
            services.chunked(2).forEach { rowItems ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    rowItems.forEach { service ->
                        ServiceCard(service, Modifier.weight(1f))
                    }
                    if (rowItems.size < 2) Spacer(modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ServiceCard(service: ServiceItem, modifier: Modifier) {
    Card(
        modifier = modifier.height(260.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(64.dp),
                shape = RoundedCornerShape(16.dp),
                color = HopeGreen.copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = service.icon,
                        contentDescription = null,
                        tint = HopeGreen,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = service.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = service.description,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )
        }
    }
}

data class ServiceItem(val icon: ImageVector, val title: String, val description: String)
