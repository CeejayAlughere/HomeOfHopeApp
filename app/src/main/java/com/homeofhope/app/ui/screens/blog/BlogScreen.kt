package com.homeofhope.app.ui.screens.blog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.homeofhope.app.ui.components.PageHeader
import com.homeofhope.app.ui.theme.HopeGreen
import com.homeofhope.app.ui.theme.HopeWhite

@Composable
fun BlogScreen(onReadMoreClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        PageHeader(title = "Our Blog", imageUrl = "https://images.unsplash.com/photo-1499750310107-5fef28a66643?q=80&w=2070")
        
        Column(modifier = Modifier.padding(16.dp)) {
            val blogs = listOf(
                BlogPost("How Our Feeding Programme Is Changing Lives in Thembisa", "Every morning, dozens of children arrive at our centre knowing they will receive a warm, nutritious meal. We share the stories of the families whose lives have been transformed by your support.", "https://images.unsplash.com/photo-1488521787991-ed7bbaae773c?q=80&w=2070"),
                BlogPost("Building a Stronger Community through Unity", "The spirit of Ubuntu is alive and well in our community. We explore how coming together as one can create lasting change and hope for everyone.", "https://images.unsplash.com/photo-1531206715517-5c0ba140b2b8?q=80&w=2070"),
                BlogPost("Why Safe Shelter Is About More Than Just a Roof", "For many of the families we support, having a safe place to sleep is just the beginning. We explore how stable shelter opens the door to education, health and a hopeful future.", "https://images.unsplash.com/photo-1469571486292-0ba58a3f068b?q=80&w=2070")
            )
            
            blogs.forEach { blog ->
                BlogCard(blog, onReadMoreClick)
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun BlogCard(blog: BlogPost, onReadMoreClick: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(blog.imageUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_logo),
                error = painterResource(R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentScale = ContentScale.Crop
            )
            
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = blog.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = blog.preview,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(20.dp))
                TextButton(
                    onClick = { onReadMoreClick(blog.title) },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Read Article →",
                        style = MaterialTheme.typography.labelLarge,
                        color = HopeGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

data class BlogPost(val title: String, val preview: String, val imageUrl: String)
