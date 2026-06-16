package com.homeofhope.app.ui.screens.causes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.homeofhope.app.ui.theme.HopeDark
import com.homeofhope.app.ui.theme.HopeGreen
import com.homeofhope.app.ui.theme.HopeWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CauseDetailScreen(
    causeId: String,
    onBackClick: () -> Unit,
    onDonateClick: () -> Unit
) {
    // In a real app, this would come from a ViewModel or Repository
    val cause = getCauseById(causeId) ?: return

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cause Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = HopeWhite,
                    titleContentColor = HopeDark
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(cause.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = cause.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = HopeDark
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                val progress = (cause.raised / cause.goal).toFloat()
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(CircleShape),
                    color = HopeGreen,
                    trackColor = HopeGreen.copy(alpha = 0.1f)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "Raised", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.outline)
                        Text(text = "R${cause.raised.toInt()}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = HopeGreen)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "Goal", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.outline)
                        Text(text = "R${cause.goal.toInt()}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                Text(
                    text = "About this cause",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = HopeDark
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = cause.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 26.sp
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "How your donation helps",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = HopeDark
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                ImpactPoint("R100 provides 5 warm meals for a child")
                ImpactPoint("R500 supports a child's school supplies for a term")
                ImpactPoint("R1000 contributes to safe shelter and healthcare")

                Spacer(modifier = Modifier.height(32.dp))
                
                Button(
                    onClick = onDonateClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = HopeGreen),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Donate to this Cause", style = MaterialTheme.typography.titleMedium, color = HopeWhite)
                }
                
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun ImpactPoint(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(HopeGreen)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}

// Helper to get cause data - in real app this would be in a ViewModel/Repo
fun getCauseById(id: String): Cause? {
    val causes = listOf(
        Cause("Feed A Hungry Child", 6000.0, 10000.0, "Thousands of children in our community go to school on an empty stomach. Help us provide daily nutritious meals to keep them focused and healthy. Our feeding program aims to reach over 500 children daily across three local schools.", "https://images.unsplash.com/photo-1488521787991-ed7bbaae773c?q=80&w=2070"),
        Cause("Clean Water For All", 8500.0, 10000.0, "Many families still lack access to safe drinking water. Your donation brings us closer to installing clean water facilities in communities that need them most. We are currently working on a borehole project that will serve 200 households.", "https://images.unsplash.com/photo-1594708767771-a7502209ff51?q=80&w=2070"),
        Cause("Education For Every Child", 14000.0, 20000.0, "A lack of school supplies and resources should never stand between a child and their future. Help us equip learners with the tools they need to succeed. We provide stationery, textbooks, and school uniforms to underprivileged students.", "https://images.unsplash.com/photo-1497633762265-9d179a990aa6?q=80&w=2073"),
        Cause("Safe Shelter For Families", 4500.0, 15000.0, "No family should sleep without a roof over their heads. We are raising funds to expand our shelter facilities and offer more families a safe place to stay. Our goal is to add 5 new family units and a community counseling room.", "https://images.unsplash.com/photo-1460317442991-0ec209397118?q=80&w=2070")
    )
    return causes.find { it.title == id }
}
