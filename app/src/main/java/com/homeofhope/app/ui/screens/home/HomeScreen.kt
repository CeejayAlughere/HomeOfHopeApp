package com.homeofhope.app.ui.screens.home

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.homeofhope.app.R
import com.homeofhope.app.ui.components.HopeButton
import com.homeofhope.app.ui.components.HopeCard
import com.homeofhope.app.ui.components.SectionHeader
import com.homeofhope.app.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    onContactClick: () -> Unit,
    onBecomeMemberClick: () -> Unit,
    onViewAllCausesClick: () -> Unit,
    onViewAllEventsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        HomeCarousel()
        
        Spacer(modifier = Modifier.height(24.dp))
        
        ImpactStatsSection()
        
        Spacer(modifier = Modifier.height(32.dp))
        
        QuickActionButtons(onBecomeMemberClick)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        FeaturedCausesSection(onViewAllCausesClick)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        UpcomingEventsSection(onViewAllEventsClick)
        
        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Composable
fun ImpactStatsSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "OUR REAL WORLD IMPACT",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ImpactCard(
                count = "5,240+",
                label = "Lives Impacted",
                icon = Icons.Default.Favorite,
                modifier = Modifier.weight(1f)
            )
            ImpactCard(
                count = "12.5k",
                label = "Meals Served",
                icon = Icons.Default.Restaurant,
                modifier = Modifier.weight(1f)
            )
            ImpactCard(
                count = "75%",
                label = "Goal Reached",
                icon = Icons.Default.TrendingUp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun ImpactCard(count: String, label: String, icon: ImageVector, modifier: Modifier) {
    HopeCard(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
        elevation = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = count,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun FeaturedCausesSection(onViewAllCausesClick: () -> Unit) {
    val featuredCauses = listOf(
        HomeCause("Feed A Hungry Child", 6000.0, 10000.0, "https://images.unsplash.com/photo-1488521787991-ed7bbaae773c?q=80&w=2070"),
        HomeCause("Clean Water For All", 8500.0, 10000.0, "https://images.unsplash.com/photo-1594708767771-a7502209ff51?q=80&w=2070"),
        HomeCause("Safe Shelter Facilities", 4500.0, 15000.0, "https://images.unsplash.com/photo-1460317442991-0ec209397118?q=80&w=2070")
    )

    Column {
        SectionHeader(
            subtitle = "Featured",
            title = "Current Causes",
            actionText = "View All",
            onActionClick = onViewAllCausesClick
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(featuredCauses) { cause ->
                FeaturedCauseCard(cause)
            }
        }
    }
}

@Composable
fun FeaturedCauseCard(cause: HomeCause) {
    HopeCard(
        modifier = Modifier.width(280.dp),
        elevation = 4.dp
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(cause.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = cause.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(12.dp))
                
                val progress = (cause.raised / cause.goal).toFloat()
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "RAISED",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "R${cause.raised.toInt()}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "GOAL",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "R${cause.goal.toInt()}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UpcomingEventsSection(onViewAllEventsClick: () -> Unit) {
    val homeEvents = listOf(
        HomeEvent("Community Food Drive", "21", "JUL", "08:00 AM • Thembisa", "https://images.unsplash.com/photo-1593113598332-cd288d649433?q=80&w=2070"),
        HomeEvent("Youth Skills Day", "01", "APR", "11:00 AM • Thembisa", "https://images.unsplash.com/photo-1529070538774-1843cb3265df?q=80&w=2070"),
        HomeEvent("Winter Blanket Drive", "04", "MAY", "10:00 AM • Thembisa", "https://images.unsplash.com/photo-1542810634-71277d95dcbb?q=80&w=2070")
    )

    Column {
        SectionHeader(
            subtitle = "Get Involved",
            title = "Upcoming Events",
            actionText = "View All",
            onActionClick = onViewAllEventsClick
        )
        
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(homeEvents) { event ->
                HomeEventCard(event)
            }
        }
    }
}

@Composable
fun HomeEventCard(event: HomeEvent) {
    HopeCard(
        modifier = Modifier.width(300.dp),
        elevation = 4.dp
    ) {
        Column {
            Box(modifier = Modifier.height(160.dp)) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(event.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                // Date Badge
                Surface(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopEnd),
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(12.dp),
                    shadowElevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = event.day,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = event.month,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = event.timeLocation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeCarousel() {
    val pagerState = rememberPagerState()
    val carouselItems = listOf(
        CarouselItem(
            imageUrl = "https://images.unsplash.com/photo-1488521787991-ed7bbaae773c?q=80&w=2070",
            heading = "Every child deserves a safe place to call home",
            subtext = "At Siyazigabisa Home Of Hope, we provide shelter, nourishment and care to vulnerable children."
        ),
        CarouselItem(
            imageUrl = "https://images.unsplash.com/photo-1509099836639-18ba1795216d?q=80&w=2062",
            heading = "Together we can break the cycle of poverty",
            subtext = "Your support helps us deliver food, healthcare and education to disadvantaged communities."
        ),
        CarouselItem(
            imageUrl = "https://images.unsplash.com/photo-1593113598332-cd288d649433?q=80&w=2070",
            heading = "Be the reason someone finds hope today",
            subtext = "Join our community and help make a real, lasting difference in the lives of others."
        )
    )
    LaunchedEffect(pagerState) {
        while (true) {
            delay(9000)
            val nextPage = (pagerState.currentPage + 1) % carouselItems.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Box(modifier = Modifier.height(400.dp)) {
        HorizontalPager(
            count = carouselItems.size,
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            CarouselSlide(carouselItems[page])
        }
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(32.dp),
            activeColor = MaterialTheme.colorScheme.primary,
            inactiveColor = Color.White.copy(alpha = 0.3f),
            indicatorWidth = 32.dp,
            indicatorHeight = 6.dp,
            spacing = 8.dp
        )
    }
}

@Composable
fun CarouselSlide(item: CarouselItem) {
    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(item.imageUrl)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.ic_logo),
            error = painterResource(R.drawable.ic_logo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f)),
                        startY = 400f
                    )
                )
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(24.dp)
                .padding(bottom = 48.dp)
        ) {
            Surface(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(
                    text = "FEATURED IMPACT",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
            Text(
                text = item.heading,
                color = Color.White,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                lineHeight = 42.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = item.subtext,
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 24.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun QuickActionButtons(
    onBecomeMemberClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        HopeButton(
            text = "Become a Member",
            icon = Icons.Default.Favorite,
            onClick = onBecomeMemberClick
        )
    }
}

data class HomeCause(val title: String, val raised: Double, val goal: Double, val imageUrl: String)

data class HomeEvent(val title: String, val day: String, val month: String, val timeLocation: String, val imageUrl: String)

data class CarouselItem(
    val imageUrl: String,
    val heading: String,
    val subtext: String
)
