package com.homeofhope.app.ui.screens.faq

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.homeofhope.app.ui.components.PageHeader
import com.homeofhope.app.ui.theme.HopeGreen

@Composable
fun FaqScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        PageHeader(title = "Frequently Asked Questions", imageUrl = "https://images.unsplash.com/photo-1523240795612-9a054b0db644?q=80&w=2070")
        
        Column(modifier = Modifier.padding(16.dp)) {
            val faqs = listOf(
                FAQItem("What is Siyazigabisa Home Of Hope?", "Siyazigabisa Home Of Hope is a non-profit organisation based in Tembisa that provides shelter, food, healthcare, education and social support to vulnerable individuals and families in our community."),
                FAQItem("How do I find out about upcoming events?", "All upcoming events are listed in the Events section of this app. You can also subscribe to our newsletter to receive event announcements directly to your inbox."),
                FAQItem("Is my personal information safe?", "Yes. We take your privacy seriously. Your personal information is stored securely and is never shared or sold to third parties. Please read our Privacy Policy for full details."),
                FAQItem("How can I contact the organisation directly?", "You can reach us through the Contact page in this app, or call us directly on 084 212 4937. You can also email us at info@homeofhope.com. We aim to respond within 24 hours."),
                FAQItem("Can I donate goods or services?", "Absolutely! In-kind donations of non-perishable food, clothing, school supplies and household goods are always welcome. Please contact us to arrange a drop-off or collection.")
            )
            
            faqs.forEach { faq ->
                FaqExpandableItem(faq)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun FaqExpandableItem(faq: FAQItem) {
    var expanded by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (expanded) HopeGreen.copy(alpha = 0.05f) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (expanded) 0.dp else 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = faq.question,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (expanded) HopeGreen else MaterialTheme.colorScheme.onSurface
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = if (expanded) HopeGreen else MaterialTheme.colorScheme.outline
                )
            }
            
            AnimatedVisibility(visible = expanded) {
                Text(
                    text = faq.answer,
                    modifier = Modifier.padding(top = 12.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 22.sp
                )
            }
        }
    }
}

data class FAQItem(val question: String, val answer: String)
