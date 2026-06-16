package com.homeofhope.app.ui.screens.donate

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var phoneNumber by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedPreset by remember { mutableStateOf("R50") }
    var frequency by remember { mutableStateOf("One-time") }
    var paymentMethod by remember { mutableStateOf("Credit Card") }
    var message by remember { mutableStateOf("") }
    
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

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            PageHeader(title = "Donate Now", imageUrl = "https://images.unsplash.com/photo-1532629345422-7515f3d16bb6?q=80&w=2070")
            
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Every rand you give goes directly towards feeding hungry children, providing shelter for homeless families and supporting vulnerable members of our community in Thembisa.",
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 22.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = "Donation Form",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Your Full Name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                label = { Text("Email") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp)
                            )
                            OutlinedTextField(
                                value = phoneNumber,
                                onValueChange = { phoneNumber = it },
                                label = { Text("Phone") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Donation Frequency", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            listOf("One-time", "Monthly").forEach { opt ->
                                OutlinedButton(
                                    onClick = { frequency = opt },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = if (frequency == opt) ButtonDefaults.outlinedButtonColors(containerColor = HopeGreen.copy(alpha = 0.1f), contentColor = HopeGreen) 
                                             else ButtonDefaults.outlinedButtonColors()
                                ) {
                                    Text(opt)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Select Amount", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            val presets = listOf("R10", "R50", "R100", "Custom")
                            presets.forEach { preset ->
                                FilterChip(
                                    selected = selectedPreset == preset,
                                    onClick = { selectedPreset = preset },
                                    label = { Text(preset) },
                                    shape = RoundedCornerShape(8.dp),
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
                                label = { Text("Enter amount (R)") },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Payment Method", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        val methods = listOf("Credit Card", "Bank Transfer", "PayPal")
                        methods.forEach { method ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                            ) {
                                RadioButton(selected = paymentMethod == method, onClick = { paymentMethod = method })
                                Text(text = method, modifier = Modifier.padding(start = 8.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = message,
                            onValueChange = { message = it },
                            label = { Text("Leave a message (Optional)") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3,
                            shape = RoundedCornerShape(12.dp)
                        )
                        
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        Button(
                            onClick = {
                                val finalAmount = if (selectedPreset == "Custom") amount.toDoubleOrNull() ?: 0.0 
                                                 else selectedPreset.replace("R", "").toDoubleOrNull() ?: 0.0
                                
                                if (Validator.isNotBlank(name) && Validator.isValidEmail(email) && Validator.isNotBlank(phoneNumber) && finalAmount > 0) {
                                    viewModel.submitDonation(
                                        name = name,
                                        email = email,
                                        phoneNumber = phoneNumber,
                                        amount = finalAmount,
                                        frequency = frequency,
                                        paymentMethod = paymentMethod,
                                        message = message.takeIf { it.isNotBlank() }
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = HopeGreen),
                            shape = RoundedCornerShape(12.dp),
                            enabled = uiState !is UiState.Loading
                        ) {
                            if (uiState is UiState.Loading) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = HopeWhite)
                            } else {
                                Text("Donate Now", color = HopeWhite, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.padding(bottom = 80.dp) // Offset for bottom nav
        )
    }
}
