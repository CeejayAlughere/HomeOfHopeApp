package com.homeofhope.app.ui.screens.donation

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.homeofhope.app.ui.components.*
import com.homeofhope.app.ui.theme.*
import com.homeofhope.app.ui.viewmodel.DonationViewModel
import com.homeofhope.app.ui.viewmodel.MemberViewModel
import com.homeofhope.app.ui.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonationScreen(
    viewModel: DonationViewModel,
    memberViewModel: MemberViewModel,
    onDonationSuccess: () -> Unit
) {
    val currentMember = memberViewModel.currentUser
    
    var fullName by remember { mutableStateOf(currentMember?.fullName ?: "") }
    var selectedBank by remember { mutableStateOf(currentMember?.savedBankName ?: "") }
    var cardNumber by remember { mutableStateOf(currentMember?.savedCardNumber ?: "") }
    var expiryDate by remember { mutableStateOf(currentMember?.savedExpiryDate ?: "") }
    var cvv by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    
    var expanded by remember { mutableStateOf(false) }
    val banks = listOf("Standard Bank", "Absa", "FNB", "Nedbank", "Capitec", "Investec")
    val presetAmounts = listOf("100", "250", "500", "1000")

    val uiState = viewModel.uiState

    LaunchedEffect(uiState) {
        if (uiState is UiState.Success) {
            memberViewModel.updatePaymentInfo(selectedBank, cardNumber, expiryDate)
            onDonationSuccess()
            viewModel.resetState()
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Support Us",
                canNavigateBack = true,
                onBack = { viewModel.resetState() }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.primary)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolunteerActivism,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Fuel the Mission",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Your contribution changes lives",
                        color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                if (currentMember?.savedCardNumber != null) {
                    HopeCard(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                        elevation = 0.dp,
                        modifier = Modifier.padding(bottom = 24.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CreditCard, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Saved Payment Method",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "Card ending in ${cardNumber.takeLast(4)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            TextButton(onClick = {
                                selectedBank = ""
                                cardNumber = ""
                                expiryDate = ""
                                cvv = ""
                            }) {
                                Text("Change", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                SectionHeader(subtitle = "Donor Info", title = "Personal Details")
                
                HopeTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = "Full Name",
                    leadingIcon = Icons.Default.Person
                )

                Spacer(modifier = Modifier.height(32.dp))

                SectionHeader(subtitle = "Payment", title = "Payment Information")

                // Bank Selection
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedBank,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select Your Bank") },
                        leadingIcon = { Icon(Icons.Default.AccountBalance, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        banks.forEach { bank ->
                            DropdownMenuItem(
                                text = { Text(bank) },
                                onClick = {
                                    selectedBank = bank
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                HopeTextField(
                    value = cardNumber,
                    onValueChange = { if (it.length <= 16) cardNumber = it },
                    label = "Card Number",
                    leadingIcon = Icons.Default.CreditCard,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    HopeTextField(
                        value = expiryDate,
                        onValueChange = { if (it.length <= 5) expiryDate = it },
                        label = "Expiry (MM/YY)",
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    HopeTextField(
                        value = cvv,
                        onValueChange = { if (it.length <= 3) cvv = it },
                        label = "CVV",
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                SectionHeader(subtitle = "Impact", title = "Donation Amount")

                // Preset Amounts
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    presetAmounts.forEach { preset ->
                        val isSelected = amount == preset
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .clickable { amount = preset },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            border = if (isSelected) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "R$preset",
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                HopeTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = "Custom Amount (ZAR)",
                    leadingIcon = Icons.Default.Payments,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )

                AnimatedVisibility(visible = uiState is UiState.Error) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = (uiState as? UiState.Error)?.message ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                HopeButton(
                    text = "CONFIRM DONATION",
                    onClick = {
                        viewModel.submitDonation(
                            memberId = currentMember?.id,
                            fullName = fullName,
                            bankName = selectedBank,
                            cardNumber = cardNumber,
                            expiryDate = expiryDate,
                            cvv = cvv,
                            amount = amount
                        )
                    },
                    isLoading = uiState is UiState.Loading
                )
                
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}
