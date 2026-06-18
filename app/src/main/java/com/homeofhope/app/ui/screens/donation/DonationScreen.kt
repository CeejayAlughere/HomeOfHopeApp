package com.homeofhope.app.ui.screens.donation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.homeofhope.app.ui.theme.HopeDark
import com.homeofhope.app.ui.theme.HopeGreen
import com.homeofhope.app.ui.theme.HopeGrey
import com.homeofhope.app.ui.theme.HopeWhite
import com.homeofhope.app.ui.viewmodel.DonationViewModel
import com.homeofhope.app.ui.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonationScreen(
    viewModel: DonationViewModel,
    onDonationSuccess: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var selectedBank by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    
    var expanded by remember { mutableStateOf(false) }
    val banks = listOf("Standard Bank", "Absa", "FNB", "Nedbank", "Capitec", "Investec")

    val uiState = viewModel.uiState

    LaunchedEffect(uiState) {
        if (uiState is UiState.Success) {
            onDonationSuccess()
            viewModel.resetState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(HopeDark),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.VolunteerActivism,
                    contentDescription = null,
                    tint = HopeGreen,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Make a Donation",
                    color = HopeWhite,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Personal Details",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = HopeDark
            )

            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = HopeGreen) },
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Payment Information",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = HopeDark
            )

            // Bank Selection Dropdown
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedBank,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select Bank") },
                    leadingIcon = { Icon(Icons.Default.AccountBalance, contentDescription = null, tint = HopeGreen) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
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

            OutlinedTextField(
                value = cardNumber,
                onValueChange = { if (it.length <= 16) cardNumber = it },
                label = { Text("Credit Card Number") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.CreditCard, contentDescription = null, tint = HopeGreen) },
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = expiryDate,
                    onValueChange = { if (it.length <= 5) expiryDate = it },
                    label = { Text("Expiry (MM/YY)") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                OutlinedTextField(
                    value = cvv,
                    onValueChange = { if (it.length <= 3) cvv = it },
                    label = { Text("CVV") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Donation Amount",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = HopeDark
            )

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount (ZAR)") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Payments, contentDescription = null, tint = HopeGreen) },
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                prefix = { Text("R ") }
            )

            if (uiState is UiState.Error) {
                Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.submitDonation(fullName, selectedBank, cardNumber, expiryDate, cvv, amount)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = HopeGreen),
                enabled = uiState !is UiState.Loading
            ) {
                if (uiState is UiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = HopeWhite)
                } else {
                    Text("MAKE DONATION", fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
