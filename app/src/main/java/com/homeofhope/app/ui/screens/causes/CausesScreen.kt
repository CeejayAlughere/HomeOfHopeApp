package com.homeofhope.app.ui.screens.causes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.homeofhope.app.R
import com.homeofhope.app.ui.components.FooterSection
import com.homeofhope.app.ui.components.NewsletterSection
import com.homeofhope.app.ui.components.PageHeader
import com.homeofhope.app.ui.theme.HopeDark
import com.homeofhope.app.ui.theme.HopeGreen
import com.homeofhope.app.ui.theme.HopeWhite

@Composable
fun CausesScreen(onDonateClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        PageHeader(title = "Our Causes", imageUrl = "https://images.unsplash.com/photo-1532629345422-7515f3d16bb6?q=80&w=2070")
        
        Column(modifier = Modifier.padding(16.dp)) {
            val causes = listOf(
                Cause("Feed A Hungry Child", 6000.0, 10000.0, "Thousands of children in our community go to school on an empty stomach. Help us provide daily nutritious meals to keep them focused and healthy.", "https://images.unsplash.com/photo-1488521787991-ed7bbaae773c?q=80&w=2070"),
                Cause("Clean Water For All", 8500.0, 10000.0, "Many families still lack access to safe drinking water. Your donation brings us closer to installing clean water facilities in communities that need them most.", "https://images.unsplash.com/photo-1594708767771-a7502209ff51?q=80&w=2070"),
                Cause("Education For Every Child", 14000.0, 20000.0, "A lack of school supplies and resources should never stand between a child and their future. Help us equip learners with the tools they need to succeed.", "https://images.unsplash.com/photo-1497633762265-9d179a990aa6?q=80&w=2073"),
                Cause("Safe Shelter For Families", 4500.0, 15000.0, "No family should sleep without a roof over their heads. We are raising funds to expand our shelter facilities and offer more families a safe place to stay.", "https://images.unsplash.com/photo-1460317442991-0ec209397118?q=80&w=2070")
            )
            
            causes.forEach { cause ->
                CauseCard(cause, onDonateClick)
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
        
    }
}

@Composable
fun CauseCard(cause: Cause, onDonateClick: () -> Unit) {
    val progress = (cause.raised / cause.goal).toFloat()
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(cause.imageUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_logo),
                error = painterResource(R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(220.dp),
                contentScale = ContentScale.Crop
            )
            
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = cause.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(10.dp).clip(CircleShape),
                    color = HopeGreen,
                    trackColor = HopeGreen.copy(alpha = 0.1f)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "Raised", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                        Text(text = "R${cause.raised.toInt()}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = HopeGreen)
                    }
                    Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {
                        Text(text = "Goal", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                        Text(text = "R${cause.goal.toInt()}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                }
                
                Text(
                    text = cause.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 22.sp
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(
                        onClick = { /* Learn More */ },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Learn More")
                    }
                    Button(
                        onClick = onDonateClick,
                        modifier = Modifier.weight(1f).height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = HopeGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Donate Now", color = HopeWhite)
                    }
                }
            }
        }
    }
}

data class Cause(val title: String, val raised: Double, val goal: Double, val description: String, val imageUrl: String)
