package com.homeofhope.app.ui.screens.contact

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.homeofhope.app.ui.components.FooterSection
import com.homeofhope.app.ui.components.NewsletterSection
import com.homeofhope.app.ui.components.PageHeader
import com.homeofhope.app.ui.theme.HopeGreen
import com.homeofhope.app.ui.theme.HopeWhite
import com.homeofhope.app.ui.viewmodel.ContactViewModel
import com.homeofhope.app.ui.viewmodel.UiState
import com.homeofhope.app.utils.Validator

@Composable
fun ContactScreen(viewModel: ContactViewModel) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState = viewModel.uiState

    LaunchedEffect(uiState) {
        if (uiState is UiState.Success) {
            snackbarHostState.showSnackbar("Thank you! Your message has been received. We will get back to you shortly.")
            viewModel.resetState()
        } else if (uiState is UiState.Error) {
            snackbarHostState.showSnackbar("Error: ${uiState.message}")
            viewModel.resetState()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            PageHeader(title = "Contact Us", imageUrl = "https://images.unsplash.com/photo-1534536281715-e28d76689b4d?q=80&w=2070")
            
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Get In Touch", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(text = "Contact for any query", fontSize = 14.sp, color = HopeGreen)
                
                Spacer(modifier = Modifier.height(24.dp))
                
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Your Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Your Email") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    label = { Text("Subject") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("Message") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 5
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = {
                        if (Validator.isNotBlank(name) && Validator.isValidEmail(email) && Validator.isNotBlank(subject) && Validator.isNotBlank(message)) {
                            viewModel.submitContact(name, email, subject, message)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = HopeGreen),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp),
                    enabled = uiState !is UiState.Loading
                ) {
                    if (uiState is UiState.Loading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = HopeWhite)
                    } else {
                        Text("Send Message", color = HopeWhite)
                    }
                }
            }
            
            NewsletterSection()
        }
    }
}
