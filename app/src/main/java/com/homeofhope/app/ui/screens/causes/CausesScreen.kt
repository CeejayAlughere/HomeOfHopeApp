package com.homeofhope.app.ui.screens.causes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
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
                modifier = Modifier.fillMaxWidth().height(180.dp),
                contentScale = ContentScale.Crop
            )
            
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = cause.title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(8.dp),
                    color = HopeGreen,
                    trackColor = HopeGreen.copy(alpha = 0.2f)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Raised: R${cause.raised.toInt()}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = HopeGreen)
                    Text(text = "Goal: R${cause.goal.toInt()}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                
                Text(text = cause.description, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = { /* Learn More */ },
                        modifier = Modifier.weight(1f),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
                    ) {
                        Text("Learn More")
                    }
                    Button(
                        onClick = onDonateClick,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = HopeGreen),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
                    ) {
                        Text("Donate Now", color = HopeWhite)
                    }
                }
            }
        }
    }
}

data class Cause(val title: String, val raised: Double, val goal: Double, val description: String, val imageUrl: String)
