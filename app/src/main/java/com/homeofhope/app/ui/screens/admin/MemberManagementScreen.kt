package com.homeofhope.app.ui.screens.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.homeofhope.app.data.local.entity.Member
import com.homeofhope.app.ui.viewmodel.AdminViewModel

@Composable
fun MemberManagementScreen(
    viewModel: AdminViewModel,
    onMemberClick: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    val members by viewModel.members.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    
    val filteredMembers = members.filter {
        it.fullName.contains(searchQuery, ignoreCase = true) ||
        it.email.contains(searchQuery, ignoreCase = true) ||
        it.memberNumber.contains(searchQuery, ignoreCase = true)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text("Member Management", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search members...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(filteredMembers) { member ->
                MemberItem(member = member, onClick = { onMemberClick(member.id) })
            }
        }
    }
}

@Composable
fun MemberItem(member: Member, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = member.fullName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text(text = member.email, style = MaterialTheme.typography.bodyMedium)
                Text(text = "ID: ${member.memberNumber}", style = MaterialTheme.typography.bodySmall)
            }
            Surface(
                color = if (member.status == "Active") MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = member.status,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null)
        }
    }
}
