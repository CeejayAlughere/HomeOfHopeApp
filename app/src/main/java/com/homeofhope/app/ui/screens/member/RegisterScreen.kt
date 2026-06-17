package com.homeofhope.app.ui.screens.member

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.homeofhope.app.ui.viewmodel.AuthState
import com.homeofhope.app.ui.viewmodel.MemberViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: MemberViewModel,
    onRegisterSuccess: () -> Unit,
    onBackClick: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var idNumber by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.Registered) {
            onRegisterSuccess()
            viewModel.clearAuthState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Become a Member") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Join Home of Hope",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Fill in your details to register as a member",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.secondary
            )

            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = idNumber,
                onValueChange = { if (it.length <= 13) idNumber = it },
                label = { Text("ID Number (13 digits)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )

            if (authState is AuthState.Error) {
                Text(
                    text = (authState as AuthState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp
                )
            }

            Button(
                onClick = {
                    viewModel.register(fullName, idNumber, username, password, confirmPassword)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = authState !is AuthState.Loading
            ) {
                if (authState is AuthState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Register")
                }
            }

            TextButton(onClick = onBackClick) {
                Text("Already have an account? Login")
            }
        }
    }
}
