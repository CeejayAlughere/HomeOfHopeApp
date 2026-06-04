package com.homeofhope.app.ui.screens.volunteer

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
import com.homeofhope.app.ui.viewmodel.VolunteerViewModel
import com.homeofhope.app.ui.viewmodel.UiState
import com.homeofhope.app.utils.Validator

@Composable
fun VolunteerScreen(viewModel: VolunteerViewModel) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState = viewModel.uiState

    LaunchedEffect(uiState) {
        if (uiState is UiState.Success) {
            snackbarHostState.showSnackbar("Thank you! You have successfully signed up as a volunteer.")
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
            PageHeader(title = "Become A Volunteer", imageUrl = "https://images.unsplash.com/photo-1559027615-cd26736f5df8?q=80&w=2070")
            
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Your Full Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Your Email Address") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    label = { Text("Tell us why you'd like to volunteer with us…") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = {
                        if (Validator.isNotBlank(name) && Validator.isValidEmail(email) && Validator.isNotBlank(reason)) {
                            viewModel.submitVolunteer(name, email, reason)
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
                        Text("Become A Volunteer", color = HopeWhite)
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Text(
                    text = "At Siyazigabisa Home Of Hope, our volunteers are the heartbeat of everything we do. Whether you help serve meals, assist with educational programmes, support our shelter residents or simply show up with a kind heart, your time and effort means the world to the people we serve. No special skills are required, only a willingness to give back to your community. Fill in the form and take the first step towards making a lasting impact.",
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            NewsletterSection()
            FooterSection()
        }
    }
}
