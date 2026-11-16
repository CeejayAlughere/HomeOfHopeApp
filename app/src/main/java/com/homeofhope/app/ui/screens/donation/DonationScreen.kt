package com.homeofhope.app.ui.screens.donation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.homeofhope.app.ui.components.AppTopBar
import com.homeofhope.app.ui.components.HopeButton
import com.homeofhope.app.ui.components.HopeTextField
import com.homeofhope.app.ui.components.SectionHeader
import com.homeofhope.app.ui.viewmodel.CauseViewModel
import com.homeofhope.app.ui.viewmodel.DonationViewModel
import com.homeofhope.app.ui.viewmodel.MemberViewModel
import com.homeofhope.app.ui.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonationScreen(
    viewModel: DonationViewModel,
    memberViewModel: MemberViewModel,
    causeViewModel: CauseViewModel,
    initialCauseId: Int?,
    onBackClick: () -> Unit,
    onDonationSuccess: () -> Unit
) {
    val currentMember = memberViewModel.currentUser
    val causeState = causeViewModel.uiState

    var selectedCauseId by rememberSaveable(initialCauseId) {
        mutableIntStateOf(initialCauseId ?: 0)
    }
    var selectedBank by rememberSaveable {
        mutableStateOf(currentMember?.savedBankName ?: "")
    }
    var amount by rememberSaveable { mutableStateOf("") }
    var causeMenuExpanded by remember { mutableStateOf(false) }
    var bankMenuExpanded by remember { mutableStateOf(false) }

    val banks = listOf(
        "Standard Bank",
        "Absa",
        "FNB",
        "Nedbank",
        "Capitec",
        "Investec",
        "Other"
    )
    val presetAmounts = listOf("100", "250", "500", "1000")
    val selectedCause = causeState.causes.firstOrNull { it.id == selectedCauseId }
    val uiState = viewModel.uiState

    LaunchedEffect(causeState.causes, initialCauseId) {
        if (selectedCauseId == 0 && causeState.causes.isNotEmpty()) {
            selectedCauseId = initialCauseId
                ?.takeIf { requested -> causeState.causes.any { it.id == requested } }
                ?: causeState.causes.first().id
        }
    }

    LaunchedEffect(uiState) {
        if (uiState is UiState.Success) {
            onDonationSuccess()
            viewModel.resetState()
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Support a Cause",
                canNavigateBack = true,
                onBack = {
                    viewModel.resetState()
                    onBackClick()
                }
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.secondary,
                                MaterialTheme.colorScheme.primary
                            )
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
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Your donation record will be added to the cause total. " +
                            "No payment is processed in this version.",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                if (!memberViewModel.isWebsiteSession) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Please sign in to your Home of Hope account to record a donation.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                SectionHeader(subtitle = "Donor", title = "Member Details")

                OutlinedTextField(
                    value = currentMember?.fullName ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Full Name") },
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))
                SectionHeader(subtitle = "Impact", title = "Choose a Cause")

                ExposedDropdownMenuBox(
                    expanded = causeMenuExpanded,
                    onExpandedChange = { causeMenuExpanded = !causeMenuExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedCause?.title.orEmpty(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Cause") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = causeMenuExpanded
                            )
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = causeMenuExpanded,
                        onDismissRequest = { causeMenuExpanded = false }
                    ) {
                        causeState.causes.forEach { cause ->
                            DropdownMenuItem(
                                text = { Text(cause.title) },
                                onClick = {
                                    selectedCauseId = cause.id
                                    causeMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                if (causeState.errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = causeState.errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
                SectionHeader(subtitle = "Donation", title = "Donation Details")

                ExposedDropdownMenuBox(
                    expanded = bankMenuExpanded,
                    onExpandedChange = { bankMenuExpanded = !bankMenuExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedBank,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select Your Bank") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.AccountBalance,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = bankMenuExpanded
                            )
                        },
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
                        expanded = bankMenuExpanded,
                        onDismissRequest = { bankMenuExpanded = false }
                    ) {
                        banks.forEach { bank ->
                            DropdownMenuItem(
                                text = { Text(bank) },
                                onClick = {
                                    selectedBank = bank
                                    bankMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

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
                            color = if (isSelected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant
                            },
                            border = if (isSelected) null else {
                                BorderStroke(
                                    1.dp,
                                    MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                                )
                            }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "R$preset",
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) {
                                        MaterialTheme.colorScheme.onPrimary
                                    } else {
                                        MaterialTheme.colorScheme.onSurface
                                    }
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
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    )
                )

                AnimatedVisibility(visible = uiState is UiState.Error) {
                    Column {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = (uiState as? UiState.Error)?.message.orEmpty(),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                HopeButton(
                    text = "SUBMIT DONATION",
                    onClick = {
                        viewModel.submitDonation(
                            causeId = selectedCause?.id,
                            bankName = selectedBank,
                            amount = amount,
                            memberId = currentMember?.id,
                            isWebsiteSession = memberViewModel.isWebsiteSession
                        )
                    },
                    isLoading = uiState is UiState.Loading
                )

                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}
