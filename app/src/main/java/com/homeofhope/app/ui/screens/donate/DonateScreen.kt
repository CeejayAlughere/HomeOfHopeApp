package com.homeofhope.app.ui.screens.donate

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
import com.homeofhope.app.ui.viewmodel.DonateViewModel
import com.homeofhope.app.ui.viewmodel.UiState
import com.homeofhope.app.utils.Validator

@Composable
fun DonateScreen(viewModel: DonateViewModel) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedPreset by remember { mutableStateOf("R50") }
    
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState = viewModel.uiState

    LaunchedEffect(uiState) {
        if (uiState is UiState.Success) {
            snackbarHostState.showSnackbar("Thank you for your donation! Your kindness makes a real difference.")
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
            PageHeader(title = "Donate Now", imageUrl = "https://images.unsplash.com/photo-1532629345422-7515f3d16bb6?q=80&w=2070")
            
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Every rand you give goes directly towards feeding hungry children, providing shelter for homeless families and supporting vulnerable members of our community in Thembisa.",
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
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
                
                Spacer(modifier = Modifier.height(24.dp))
                Text("Select Donation Amount", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val presets = listOf("R10", "R50", "R100", "Custom")
                    presets.forEach { preset ->
                        FilterChip(
                            selected = selectedPreset == preset,
                            onClick = { selectedPreset = preset },
                            label = { Text(preset) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = HopeGreen,
                                selectedLabelColor = HopeWhite
                            )
                        )
                    }
                }
                
                if (selectedPreset == "Custom") {
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        label = { Text("Enter custom amount (R)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Button(
                    onClick = {
                        val finalAmount = if (selectedPreset == "Custom") amount.toDoubleOrNull() ?: 0.0 
                                         else selectedPreset.replace("R", "").toDoubleOrNull() ?: 0.0
                        
                        if (Validator.isNotBlank(name) && Validator.isValidEmail(email) && finalAmount > 0) {
                            viewModel.submitDonation(name, email, finalAmount)
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
                        Text("Donate Now", color = HopeWhite)
                    }
                }
            }
            
            NewsletterSection()
            FooterSection()
        }
    }
}
