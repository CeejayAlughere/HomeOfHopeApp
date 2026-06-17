package com.homeofhope.app.ui.screens.legal

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.homeofhope.app.ui.components.PageHeader

@Composable
fun PrivacyScreen() {
    LegalScreenTemplate(title = "Privacy Policy", content = """
        At Siyazigabisa Home Of Hope, we take your privacy seriously. This policy explains what data we collect and how we use it.
        
        1. Data We Collect
        We collect information you provide directly to us, such as your name, email address, and contact messages.
        
        2. How We Use Your Data
        We use your data to send newsletters (if you've subscribed), and respond to your queries.
        
        3. Data Sharing
        We do not sell or share your personal data with third parties for marketing purposes.
        
        4. Cookies and Tracking
        The app uses local storage to remember your preferences and subscription status.
        
        5. Your Rights
        You have the right to access, correct, or delete your personal data. Contact us at info@homeofhope.com for any privacy-related queries.
    """.trimIndent())
}

@Composable
fun TermsScreen() {
    LegalScreenTemplate(title = "Terms of Use", content = """
        By using the Home Of Hope application, you agree to the following terms:
        
        1. Acceptance of Terms
        Your access to and use of this app is subject to these terms and conditions.
        
        2. Use of the Application
        The app is for personal, non-commercial use only. You agree not to use it for any unlawful purpose.
        
        3. Limitation of Liability
        We are not liable for any damages arising from your use of the application.
        
        4. Governing Law
        These terms are governed by the laws of South Africa.
    """.trimIndent())
}

@Composable
fun CookiesScreen() {
    LegalScreenTemplate(title = "Cookies Policy", content = """
        This app uses local storage and cookies to improve your experience.
        
        1. What are Cookies?
        Cookies are small text files stored on your device. In this app, we use them (via DataStore) to remember your preferences.
        
        2. How We Use Them
        We use local storage to keep track of your newsletter subscription state and other user-specific settings.
        
        3. Managing Data
        You can clear the app's data in your device settings to remove all stored information.
        
        4. Third-Party Analytics
        We do not currently use any third-party tracking or analytics cookies in this application.
    """.trimIndent())
}

@Composable
fun LegalScreenTemplate(title: String, content: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        PageHeader(title = title, imageUrl = "https://images.unsplash.com/photo-1450101499163-c8848c66ca85?q=80&w=2070")
        
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = content, fontSize = 14.sp, lineHeight = 22.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
