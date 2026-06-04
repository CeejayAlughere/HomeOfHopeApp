package com.homeofhope.app.ui.screens.blog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.homeofhope.app.ui.components.FooterSection
import com.homeofhope.app.ui.components.NewsletterSection
import com.homeofhope.app.ui.components.PageHeader
import com.homeofhope.app.ui.theme.HopeGreen

@Composable
fun BlogScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        PageHeader(title = "Our Blog", imageUrl = "https://images.unsplash.com/photo-1499750310107-5fef28a66643?q=80&w=2070")
        
        Column(modifier = Modifier.padding(16.dp)) {
            val blogs = listOf(
                BlogPost("How Our Feeding Programme Is Changing Lives in Thembisa", "Every morning, dozens of children arrive at our centre knowing they will receive a warm, nutritious meal. We share the stories of the families whose lives have been transformed by your support.", "https://images.unsplash.com/photo-1488521787991-ed7bbaae773c?q=80&w=2070"),
                BlogPost("Meet the Volunteers Who Show Up Every Single Week", "Behind every meal served and every child supported is a team of dedicated volunteers. We sat down with some of our longest serving volunteers to hear why they keep coming back.", "https://images.unsplash.com/photo-1559027615-cd26736f5df8?q=80&w=2070"),
                BlogPost("Why Safe Shelter Is About More Than Just a Roof", "For many of the families we support, having a safe place to sleep is just the beginning. We explore how stable shelter opens the door to education, health and a hopeful future.", "https://images.unsplash.com/photo-1469571486292-0ba58a3f068b?q=80&w=2070")
            )
            
            blogs.forEach { blog ->
                BlogCard(blog)
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
        
        NewsletterSection()
        FooterSection()
    }
}

@Composable
fun BlogCard(blog: BlogPost) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            AsyncImage(
                model = blog.imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(180.dp),
                contentScale = ContentScale.Crop
            )
            
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = blog.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = blog.preview, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(12.dp))
                TextButton(onClick = { /* Read More */ }, contentPadding = PaddingValues(0.dp)) {
                    Text("Read More", color = HopeGreen, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

data class BlogPost(val title: String, val preview: String, val imageUrl: String)
