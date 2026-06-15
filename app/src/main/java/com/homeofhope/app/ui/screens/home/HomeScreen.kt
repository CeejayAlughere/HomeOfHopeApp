package com.homeofhope.app.ui.screens.home

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
import com.homeofhope.app.ui.components.FooterSection
import com.homeofhope.app.ui.components.NewsletterSection
import com.homeofhope.app.ui.components.SectionHeader
import com.homeofhope.app.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    onDonateClick: () -> Unit,
    onVolunteerClick: () -> Unit,
    onContactClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState())
    ) {
        HomeCarousel()
        
        Spacer(modifier = Modifier.height(16.dp))
        
        ImpactStatsSection()
        
        Spacer(modifier = Modifier.height(24.dp))
        
        QuickActionButtons(onDonateClick, onVolunteerClick, onContactClick)
        
        Spacer(modifier = Modifier.height(24.dp))
        
        FeaturedCausesSection(onDonateClick)
        
        Spacer(modifier = Modifier.height(24.dp))
        
        UpcomingEventsSection(onVolunteerClick)
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun ImpactStatsSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "OUR REAL-WORLD IMPACT",
            style = MaterialTheme.typography.labelLarge,
            color = HopeGreen,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
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
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = HopeGreen.copy(alpha = 0.05f),
        border = BorderStroke(1.dp, HopeGreen.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = HopeGreen,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = count,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = HopeDark
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = HopeGrey
            )
        }
    }
}

@Composable
fun FeaturedCausesSection(onDonateClick: () -> Unit) {
    val featuredCauses = listOf(
        HomeCause("Feed A Hungry Child", 6000.0, 10000.0, "https://images.unsplash.com/photo-1488521787991-ed7bbaae773c?q=80&w=2070"),
        HomeCause("Clean Water For All", 8500.0, 10000.0, "https://images.unsplash.com/photo-1594708767771-a7502209ff51?q=80&w=2070"),
        HomeCause("Safe Shelter Facilities", 4500.0, 15000.0, "https://images.unsplash.com/photo-1460317442991-0ec209397118?q=80&w=2070")
    )

    Column {
        PaddingValues(horizontal = 16.dp).let {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Featured Causes",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = { /* Navigate to Causes */ }) {
                    Text("View All", color = HopeGreen)
                }
            }
        }
        
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(featuredCauses) { cause ->
                FeaturedCauseCard(cause, onDonateClick)
            }
        }
    }
}

@Composable
fun FeaturedCauseCard(cause: HomeCause, onDonateClick: () -> Unit) {
    Card(
        modifier = Modifier.width(260.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = HopeLight)
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
                    .height(140.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = cause.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                val progress = (cause.raised / cause.goal).toFloat()
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                    color = HopeGreen,
                    trackColor = HopeGreen.copy(alpha = 0.1f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text("R${cause.raised.toInt()} raised", style = MaterialTheme.typography.labelSmall, color = HopeGreen, fontWeight = FontWeight.Bold)
                    Text("Target R${cause.goal.toInt()}", style = MaterialTheme.typography.labelSmall, color = HopeGrey)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onDonateClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = HopeDark),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Text("Donate Now", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

@Composable
fun UpcomingEventsSection(onVolunteerClick: () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Upcoming Events",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = HopeDark,
            tonalElevation = 8.dp
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(HopeGreen, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("21", color = HopeWhite, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("JUL", color = HopeWhite, fontSize = 12.sp)
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Community Food Drive", color = HopeWhite, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Text("08:00 AM • Thembisa", color = HopeWhite.copy(alpha = 0.7f), style = MaterialTheme.typography.bodySmall)
                }
                IconButton(
                    onClick = onVolunteerClick,
                    colors = IconButtonDefaults.iconButtonColors(containerColor = HopeGreen)
                ) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "Join", tint = HopeWhite)
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
            subtext = "At Siyazigabisa Home Of Hope, we provide shelter, nourishment and care to vulnerable children and families who need it most."
        ),
        CarouselItem(
            imageUrl = "https://images.unsplash.com/photo-1509099836639-18ba1795216d?q=80&w=2062",
            heading = "Together we can break the cycle of poverty",
            subtext = "Your support helps us deliver food, healthcare and education to disadvantaged communities across South Africa."
        ),
        CarouselItem(
            imageUrl = "https://images.unsplash.com/photo-1518391846015-55a9cc003b25?q=80&w=2070",
            heading = "Be the reason someone finds hope today",
            subtext = "Join our growing community of donors and volunteers who are making a real, lasting difference in the lives of others."
        )
    )

    LaunchedEffect(pagerState) {
        while (true) {
            delay(9000)
            val nextPage = (pagerState.currentPage + 1) % carouselItems.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Box(modifier = Modifier.height(350.dp)) {
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
                .padding(24.dp),
            activeColor = HopeGreen,
            inactiveColor = HopeWhite.copy(alpha = 0.3f),
            indicatorWidth = 24.dp,
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
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                        startY = 300f
                    )
                )
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(24.dp)
                .padding(bottom = 32.dp)
        ) {
            Surface(
                color = HopeGreen,
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text(
                    text = "FEATURED",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = HopeWhite,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = item.heading,
                color = HopeWhite,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                lineHeight = 34.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.subtext,
                color = HopeWhite.copy(alpha = 0.9f),
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 22.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun QuickActionButtons(
    onDonateClick: () -> Unit,
    onVolunteerClick: () -> Unit,
    onContactClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ModernActionButton(
            text = "Donate",
            icon = Icons.Default.VolunteerActivism,
            containerColor = HopeGreen,
            contentColor = HopeWhite,
            modifier = Modifier.weight(1f),
            onClick = onDonateClick
        )
        ModernActionButton(
            text = "Volunteer",
            icon = Icons.Default.Groups,
            containerColor = HopeDark,
            contentColor = HopeWhite,
            modifier = Modifier.weight(1f),
            onClick = onVolunteerClick
        )
    }
}

@Composable
fun ModernActionButton(
    text: String,
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        colors = ButtonDefaults.buttonColors(containerColor = containerColor, contentColor = contentColor),
        shape = RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontWeight = FontWeight.Bold)
    }
}

data class HomeCause(val title: String, val raised: Double, val goal: Double, val imageUrl: String)

data class CarouselItem(
    val imageUrl: String,
    val heading: String,
    val subtext: String
)
