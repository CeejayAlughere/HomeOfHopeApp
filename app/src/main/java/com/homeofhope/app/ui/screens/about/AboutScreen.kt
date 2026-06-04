package com.homeofhope.app.ui.screens.about

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.homeofhope.app.ui.components.FooterSection
import com.homeofhope.app.ui.components.NewsletterSection
import com.homeofhope.app.ui.components.PageHeader
import com.homeofhope.app.ui.components.SectionHeader
import com.homeofhope.app.ui.theme.HopeDark
import com.homeofhope.app.ui.theme.HopeGreen
import com.homeofhope.app.ui.theme.HopeLight
import com.homeofhope.app.ui.theme.HopeWhite

@Composable
fun AboutScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        PageHeader(title = "About Us", imageUrl = "https://images.unsplash.com/photo-1524069290683-0457abfe42c3?q=80&w=2070")
        AboutTabSection()
        StatsSection()
        TestimonialsSection()
        NewsletterSection()
        FooterSection()
    }
}

@Composable
fun AboutTabSection() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("About", "Mission", "Vision")
    
    Column(modifier = Modifier.padding(16.dp)) {
        SectionHeader(subtitle = "Learn More About Us", title = "Siyazigabisa Home Of Hope")
        
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.Transparent,
            contentColor = HopeGreen,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = HopeGreen
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title, fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal) }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = when (selectedTab) {
                0 -> "Siyazigabisa Home of Hope is an orphanage and community center for children located about 25 kilometres north of Johannesburg, in a township called Thembisa. The founder of the home, Gabisile Khoza, has a strong determination and will-power to provide her children with the best situation possible. She started the home in response to the violence and abuse occurring in the township. She welcomed and continues to invite abandoned and abused children, and abused women. She helps counsel these women and children that feel torn from these experiences and have had many trials and tribulations. The name Siyazigabisa means to be proud of who you are. This is the goal that Gabisile has: that everyone can feel more confident and sure of themselves."
                1 -> "Our mission is to restore hope and dignity to disadvantaged individuals and families by delivering compassionate, practical support across food security, healthcare, education and safe housing. We are committed to building a strong, caring community where no person is left behind — regardless of their background or circumstances. Every life matters to us, and we will continue fighting to make that a reality for the people we serve."
                else -> "We envision a South Africa where every person has access to the basic necessities of life — food, clean water, shelter, healthcare and education. A society where vulnerable communities are empowered to break the cycle of poverty and build sustainable futures for themselves and their children. Siyazigabisa Home Of Hope strives to be a beacon of lasting change, driven by love, unity and unwavering hope."
            },
            fontSize = 14.sp,
            lineHeight = 22.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun StatsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(HopeDark)
            .padding(24.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            StatItem("Countries", 1, Modifier.weight(1f))
            StatItem("Volunteers", 400, Modifier.weight(1f), suffix = "+")
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            StatItem("Our Goal", 10000, Modifier.weight(1f), prefix = "R")
            StatItem("Raised", 5000, Modifier.weight(1f), prefix = "R")
        }
    }
}

@Composable
fun StatItem(label: String, value: Int, modifier: Modifier, prefix: String = "", suffix: String = "") {
    var startAnimate by remember { mutableStateOf(false) }
    val animatedValue by animateIntAsState(
        targetValue = if (startAnimate) value else 0,
        animationSpec = tween(durationMillis = 2000)
    )
    
    LaunchedEffect(Unit) {
        startAnimate = true
    }
    
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Text(
            text = "$prefix$animatedValue$suffix",
            color = HopeGreen,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
        Text(text = label, color = HopeWhite, fontSize = 14.sp)
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TestimonialsSection() {
    val pagerState = rememberPagerState()
    val testimonials = listOf(
        Testimonial(
            name = "Micheal Van nie Kerk",
            role = "Community Member",
            text = "Home Of Hope gave my children and me a safe place to stay when we had nowhere else to go. The warmth and care we received here restored my faith in humanity. I am forever grateful."
        ),
        Testimonial(
            name = "Elizabeth Johnson",
            role = "Volunteer",
            text = "Volunteering at Siyazigabisa has been one of the most rewarding experiences of my life. Seeing the smiles on people's faces when you show up to help is something words cannot describe."
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SectionHeader(subtitle = "Testimonials", title = "What People Say")
        
        HorizontalPager(
            count = testimonials.size,
            state = pagerState,
            modifier = Modifier.height(200.dp)
        ) { page ->
            TestimonialCard(testimonials[page])
        }
        
        HorizontalPagerIndicator(
            pagerState = pagerState,
            activeColor = HopeGreen,
            inactiveColor = Color.Gray.copy(alpha = 0.5f)
        )
    }
}

@Composable
fun TestimonialCard(testimonial: Testimonial) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = HopeLight)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = testimonial.text,
                fontSize = 14.sp,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = testimonial.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = testimonial.role, fontSize = 12.sp, color = HopeGreen)
        }
    }
}

data class Testimonial(val name: String, val role: String, val text: String)
