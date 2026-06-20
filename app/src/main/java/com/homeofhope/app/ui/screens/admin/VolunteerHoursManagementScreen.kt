package com.homeofhope.app.ui.screens.admin

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
import com.homeofhope.app.data.local.entity.VolunteerHours
import com.homeofhope.app.ui.viewmodel.AdminViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VolunteerHoursManagementScreen(
    viewModel: AdminViewModel,
    onBackClick: () -> Unit
) {
    val members by viewModel.members.collectAsState()
    var selectedMember by remember { mutableStateOf<Member?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }

    val memberHours by if (selectedMember != null) {
        viewModel.getHoursForMember(selectedMember!!.id).collectAsState(initial = emptyList())
    } else {
        remember { mutableStateOf(emptyList<VolunteerHours>()) }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text("Volunteer Hours", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Member Selector
        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedMember?.fullName ?: "Select a Member",
                onValueChange = {},
                readOnly = true,
                label = { Text("Member") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                members.forEach { member ->
                    DropdownMenuItem(
                        text = { Text(member.fullName) },
                        onClick = {
                            selectedMember = member
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedMember != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Service History", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Button(onClick = { showAddDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Add Hours")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(memberHours) { record ->
                    HoursRecordItem(record = record, onDelete = { viewModel.deleteVolunteerHours(record) })
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Select a member to manage their hours")
            }
        }
    }

    if (showAddDialog && selectedMember != null) {
        AddHoursDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { event, hours, date, notes ->
                viewModel.addVolunteerHours(selectedMember!!.id, event, hours, date, notes)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun HoursRecordItem(record: VolunteerHours, onDelete: () -> Unit) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(record.eventName, fontWeight = FontWeight.Bold)
                Text("${record.hours} Hours • ${dateFormat.format(Date(record.serviceDate))}")
                if (record.notes.isNotEmpty()) {
                    Text(record.notes, style = MaterialTheme.typography.bodySmall)
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun AddHoursDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Double, Long, String) -> Unit
) {
    var eventName by remember { mutableStateOf("") }
    var hours by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    val date = System.currentTimeMillis()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Volunteer Hours") },
        text = {
            Column {
                OutlinedTextField(value = eventName, onValueChange = { eventName = it }, label = { Text("Event Name") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = hours, onValueChange = { hours = it }, label = { Text("Hours Served") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("Notes") })
            }
        },
        confirmButton = {
            Button(onClick = {
                val h = hours.toDoubleOrNull() ?: 0.0
                onConfirm(eventName, h, date, notes)
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
