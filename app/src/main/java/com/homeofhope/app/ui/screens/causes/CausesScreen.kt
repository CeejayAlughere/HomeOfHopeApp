package com.homeofhope.app.ui.screens.causes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.homeofhope.app.R
import com.homeofhope.app.data.model.Cause
import com.homeofhope.app.ui.components.PageHeader
import com.homeofhope.app.ui.theme.HopeGreen
import com.homeofhope.app.ui.theme.HopeWhite
import com.homeofhope.app.ui.viewmodel.CauseViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CausesScreen(
    viewModel: CauseViewModel,
    onLearnMoreClick: (Int) -> Unit
) {
    val uiState = viewModel.uiState

    LaunchedEffect(Unit) {
        if (!uiState.isLoading) {
            viewModel.refresh()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        PageHeader(
            title = "Our Causes",
            imageUrl = "https://images.unsplash.com/photo-1532629345422-7515f3d16bb6?q=80&w=2070"
        )

        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Live cause progress",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = viewModel::refresh) {
                    Text("Refresh")
                }
            }

            if (uiState.isLoading && uiState.causes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = HopeGreen)
                }
            } else if (uiState.errorMessage != null && uiState.causes.isEmpty()) {
                ErrorMessage(
                    message = uiState.errorMessage,
                    onRetry = viewModel::refresh
                )
            } else if (uiState.causes.isEmpty()) {
                Text(
                    text = "No active causes are available right now.",
                    modifier = Modifier.padding(vertical = 32.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                uiState.errorMessage?.let { message ->
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                uiState.causes.forEach { cause ->
                    CauseCard(cause, onLearnMoreClick)
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun ErrorMessage(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text("Try Again")
        }
    }
}

@Composable
fun CauseCard(
    cause: Cause,
    onLearnMoreClick: (Int) -> Unit
) {
    val progress = (cause.progress / 100.0).toFloat().coerceIn(0f, 1f)
    val currency = NumberFormat.getCurrencyInstance(Locale("en", "ZA"))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(cause.imageUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_logo),
                error = painterResource(R.drawable.ic_logo),
                contentDescription = cause.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = cause.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
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
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                        Text(
                            text = currency.format(cause.raised),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = HopeGreen
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Goal",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                        Text(
                            text = currency.format(cause.goal),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Text(
                    text = cause.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { onLearnMoreClick(cause.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = HopeGreen
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Learn More", color = HopeWhite)
                }
            }
        }
    }
}
