
package com.homeofhope.app.ui.screens.member

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.homeofhope.app.ui.viewmodel.GoogleAuthState
import com.homeofhope.app.ui.viewmodel.MemberViewModel

@Composable
fun GoogleProfileCompletionScreen(
    viewModel: MemberViewModel,
    onProfileComplete: () -> Unit
) {
    val googleAuthState by viewModel.googleAuthState.collectAsState()

    var fullName by remember { mutableStateOf("") }
    var idNumber by remember { mutableStateOf("") }
    var googleEmail by remember { mutableStateOf("") }

    val profileState = googleAuthState as? GoogleAuthState.ProfileRequired
    val isLoading = googleAuthState is GoogleAuthState.Loading
    val errorMessage =
        (googleAuthState as? GoogleAuthState.Error)?.message

    LaunchedEffect(profileState?.email, profileState?.displayName) {
        profileState?.let { profile ->
            googleEmail = profile.email

            if (fullName.isBlank()) {
                fullName = profile.displayName
            }
        }
    }

    LaunchedEffect(googleAuthState) {
        if (googleAuthState is GoogleAuthState.ExistingMember) {
            viewModel.clearGoogleAuthState()
            onProfileComplete()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Complete Your Profile",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Finish setting up your Home of Hope membership.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = googleEmail,
            onValueChange = {},
            label = { Text("Google email") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = idNumber,
            onValueChange = { value ->
                if (value.all { it.isDigit() } && value.length <= 13) {
                    idNumber = value
                }
            },
            label = { Text("South African ID number") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            enabled = !isLoading
        )

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.completeGoogleProfile(
                    fullName = fullName,
                    idNumber = idNumber
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = !isLoading &&
                    fullName.isNotBlank() &&
                    idNumber.length == 13
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Text("COMPLETE PROFILE")
            }
        }
    }
}
