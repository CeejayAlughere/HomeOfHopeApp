package com.homeofhope.app.ui.screens.blog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@Composable
fun BlogDetailScreen(
    blogId: String
) {
    val blog = getBlogById(blogId) ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(blog.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.padding(24.dp)) {
            Surface(
                color = HopeGreen.copy(alpha = 0.1f),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = "COMMUNITY NEWS",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = HopeGreen,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = blog.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = HopeDark,
                lineHeight = 32.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "By Home of Hope Team",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "•",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Oct 24, 2023",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))

            Text(
                text = blog.preview,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = HopeDark,
                lineHeight = 28.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.\n\nDuis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\n\nSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 24.sp
            )
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

fun getBlogById(id: String): BlogPost? {
    val blogs = listOf(
        BlogPost("How Our Feeding Programme Is Changing Lives in Tembisa", "Every morning, dozens of children arrive at our centre knowing they will receive a warm, nutritious meal. We share the stories of the families whose lives have been transformed by your support.", "https://images.unsplash.com/photo-1488521787991-ed7bbaae773c?q=80&w=2070"),
        BlogPost("Building a Stronger Community through Unity", "The spirit of Ubuntu is alive and well in our community. We explore how coming together as one can create lasting change and hope for everyone.", "https://images.unsplash.com/photo-1531206715517-5c0ba140b2b8?q=80&w=2070"),
        BlogPost("Why Safe Shelter Is About More Than Just a Roof", "For many of the families we support, having a safe place to sleep is just the beginning. We explore how stable shelter opens the door to education, health and a hopeful future.", "https://images.unsplash.com/photo-1469571486292-0ba58a3f068b?q=80&w=2070")
    )
    return blogs.find { it.title == id }
}
