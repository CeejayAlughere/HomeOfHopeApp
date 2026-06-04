package com.homeofhope.app.ui.screens.help

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContactSupport
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.homeofhope.app.ui.components.FooterSection
import com.homeofhope.app.ui.components.NewsletterSection
import com.homeofhope.app.ui.components.PageHeader
import com.homeofhope.app.ui.theme.HopeGreen
import com.homeofhope.app.ui.theme.HopeWhite

@Composable
fun HelpScreen(onContactClick: () -> Unit, onFaqClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        PageHeader(title = "Help & Support", imageUrl = "https://images.unsplash.com/photo-1469571486292-0ba58a3f068b?q=80&w=2070")
        
        Column(modifier = Modifier.padding(16.dp)) {
            HelpCard(Icons.Default.VolunteerActivism, "Making a Donation", "Visit the Donate section, enter your name and email, select an amount and tap Donate Now. Your contribution goes directly to supporting our community programmes.")
            Spacer(modifier = Modifier.height(16.dp))
            HelpCard(Icons.Default.VolunteerActivism, "Becoming a Volunteer", "Visit the Volunteer section and complete the form. One of our team members will reach out to you with more details about how you can get involved.")
            Spacer(modifier = Modifier.height(16.dp))
            HelpCard(Icons.Default.ContactSupport, "Getting in Touch", "Visit the Contact section and send us a message. You can also call us directly on 084 212 4937. We aim to respond within 24 hours.")
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(text = "Still need help?", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = onContactClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = HopeGreen),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
                ) {
                    Text("Contact Us", color = HopeWhite)
                }
                OutlinedButton(
                    onClick = onFaqClick,
                    modifier = Modifier.weight(1f),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
                ) {
                    Text("View FAQs")
                }
            }
        }
        
        NewsletterSection()
        FooterSection()
    }
}

@Composable
fun HelpCard(icon: ImageVector, title: String, description: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
            Icon(imageVector = icon, contentDescription = null, tint = HopeGreen, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = description, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
