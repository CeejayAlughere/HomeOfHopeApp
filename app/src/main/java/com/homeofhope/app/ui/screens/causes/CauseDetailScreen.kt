package com.homeofhope.app.ui.screens.causes

import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.homeofhope.app.data.model.Cause
import com.homeofhope.app.ui.theme.HopeDark
import com.homeofhope.app.ui.theme.HopeGreen
import com.homeofhope.app.ui.theme.HopeGrey
import com.homeofhope.app.ui.viewmodel.CauseViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CauseDetailScreen(
    causeId: Int,
    viewModel: CauseViewModel,
    isLoggedIn: Boolean,
    onDonateClick: (Int) -> Unit,
    onLoginClick: () -> Unit
) {
    val uiState = viewModel.uiState
    val cause = viewModel.findCause(causeId)

    LaunchedEffect(causeId) {
        if (cause == null && !uiState.isLoading) {
            viewModel.refresh()
        }
    }

    when {
        cause != null -> CauseDetailContent(
            cause = cause,
            isLoggedIn = isLoggedIn,
            onDonateClick = onDonateClick,
            onLoginClick = onLoginClick
        )

        uiState.isLoading -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = HopeGreen)
        }

        else -> Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = uiState.errorMessage ?: "This cause is unavailable.",
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = viewModel::refresh) {
                Text("Try Again")
            }
        }
    }
}

@Composable
private fun CauseDetailContent(
    cause: Cause,
    isLoggedIn: Boolean,
    onDonateClick: (Int) -> Unit,
    onLoginClick: () -> Unit
) {
    var showLoginDialog by remember { mutableStateOf(false) }
    val currency = NumberFormat.getCurrencyInstance(Locale("en", "ZA"))
    val progress = (cause.progress / 100.0).toFloat().coerceIn(0f, 1f)

    if (showLoginDialog) {
        AlertDialog(
            onDismissRequest = { showLoginDialog = false },
            title = { Text("Membership Required") },
            text = {
                Text(
                    "You need to be a member to make a donation. " +
                        "Log in or register to continue."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLoginDialog = false
                        onLoginClick()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = HopeGreen
                    )
                ) {
                    Text("Login / Register")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLoginDialog = false }) {
                    Text("Cancel", color = HopeGrey)
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(cause.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = cause.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = cause.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = HopeDark
            )

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(CircleShape),
                color = HopeGreen,
                trackColor = HopeGreen.copy(alpha = 0.1f)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Raised",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = currency.format(cause.raised),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = HopeGreen
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Goal",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = currency.format(cause.goal),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            Button(
                onClick = {
                    if (isLoggedIn) {
                        onDonateClick(cause.id)
                    } else {
                        showLoginDialog = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = HopeGreen
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("DONATE TO THIS CAUSE", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "About this cause",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = HopeDark
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = cause.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 26.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Project Impact",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = HopeDark
            )
            Spacer(modifier = Modifier.height(12.dp))
            ImpactPoint("R100 provides practical support to this cause")
            ImpactPoint("R500 helps the project reach more people")
            ImpactPoint("R1000 supports long-term community impact")

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ImpactPoint(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(HopeGreen)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}
