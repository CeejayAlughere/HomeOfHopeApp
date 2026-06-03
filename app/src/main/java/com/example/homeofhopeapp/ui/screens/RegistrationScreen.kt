package com.example.homeofhopeapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.homeofhopeapp.ui.auth.AuthState
import com.example.homeofhopeapp.ui.auth.AuthViewModel

@Composable
fun RegistrationScreen(
    viewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val authState by viewModel.authState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Siyazigabisa Home Of Hope", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Register", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.register(fullName, username, password, confirmPassword) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }

        TextButton(onClick = onNavigateToLogin) {
            Text("Already have an account? Login")
        }

        when (authState) {
            is AuthState.Loading -> CircularProgressIndicator()
            is AuthState.Error -> Text(
                text = (authState as AuthState.Error).message,
                color = MaterialTheme.colorScheme.error
            )
            is AuthState.Success -> {
                Text(text = (authState as AuthState.Success).message)
                LaunchedEffect(Unit) {
                    onNavigateToLogin()
                    viewModel.resetState()
                }
            }
            else -> {}
        }
    }
}
