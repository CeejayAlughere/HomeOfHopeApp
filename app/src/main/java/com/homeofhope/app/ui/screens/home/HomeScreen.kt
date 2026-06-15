package com.homeofhope.app.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.homeofhope.app.ui.theme.HopeDark
import com.homeofhope.app.ui.theme.HopeGreen
import com.homeofhope.app.ui.theme.HopeWhite
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
            .verticalScroll(rememberScrollState())
    ) {
        HomeCarousel()
        QuickActionButtons(onDonateClick, onVolunteerClick, onContactClick)
        NewsletterSection()
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

    Box(modifier = Modifier.height(300.dp)) {
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
                .padding(16.dp),
            activeColor = HopeGreen,
            inactiveColor = HopeWhite.copy(alpha = 0.5f)
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
        ) {
            Text(
                text = item.heading,
                color = HopeWhite,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 30.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.subtext,
                color = HopeWhite,
                fontSize = 14.sp,
                lineHeight = 20.sp
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
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ActionButton("Donate Now", Modifier.weight(1f), onDonateClick)
        ActionButton("Volunteer", Modifier.weight(1f), onVolunteerClick)
        ActionButton("Contact Us", Modifier.weight(1f), onContactClick)
    }
}

@Composable
fun ActionButton(text: String, modifier: Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = HopeGreen),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        Text(text = text, color = HopeWhite, fontSize = 12.sp, textAlign = TextAlign.Center)
    }
}

data class CarouselItem(
    val imageUrl: String,
    val heading: String,
    val subtext: String
)
