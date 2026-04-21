package com.example.ndejjeconnect.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ndejjeconnect.data.local.TimetableEntry
import com.example.ndejjeconnect.viewmodel.AuthViewModel
import com.example.ndejjeconnect.viewmodel.TimetableViewModel

/**
 * View Layer: Timetable Screen
 * Displays the lecture schedule with daily tabs.
 * MVVM: Observes TimetableViewModel for schedule data.
 */
@Composable
fun TimetableScreen(viewModel: TimetableViewModel, authViewModel: AuthViewModel) {
    val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    val selectedDay by viewModel.selectedDay.collectAsState()
    val entries by viewModel.timetableEntries.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()
    val availableUnits by viewModel.availableUnits.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }

    LaunchedEffect(currentUser) {
        viewModel.setUser(currentUser)
        viewModel.seedData() // Ensure basic units are in DB for demo
    }

    val selectedTabIndex = days.indexOf(selectedDay).coerceAtLeast(0)

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Class")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                edgePadding = 16.dp,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                days.forEach { day ->
                    Tab(
                        selected = selectedDay == day,
                        onClick = { viewModel.selectDay(day) },
                        text = { Text(day.take(3)) } // Show "Mon", "Tue", etc.
                    )
                }
            }

            if (entries.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No classes scheduled for $selectedDay", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(entries) { entry ->
                        TimetableCard(entry, onDelete = { viewModel.removeEntry(entry.id) })
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddTimetableEntryDialog(
            availableUnits = availableUnits,
            selectedDay = selectedDay,
            onDismiss = { showAddDialog = false },
            onSave = { name, start, end, venue ->
                viewModel.addEntry(name, selectedDay, start, end, venue)
                showAddDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTimetableEntryDialog(
    availableUnits: List<String>,
    selectedDay: String,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String) -> Unit
) {
    var selectedUnitName by remember { mutableStateOf(availableUnits.firstOrNull() ?: "") }
    var startTime by remember { mutableStateOf("08:00 AM") }
    var endTime by remember { mutableStateOf("11:00 AM") }
    var venue by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Class to $selectedDay") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedUnitName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select Unit") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        availableUnits.forEach { unit ->
                            DropdownMenuItem(
                                text = { Text(unit) },
                                onClick = {
                                    selectedUnitName = unit
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(value = startTime, onValueChange = { startTime = it }, label = { Text("Start Time") })
                OutlinedTextField(value = endTime, onValueChange = { endTime = it }, label = { Text("End Time") })
                OutlinedTextField(value = venue, onValueChange = { venue = it }, label = { Text("Venue") })
            }
        },
        confirmButton = {
            Button(onClick = { onSave(selectedUnitName, startTime, endTime, venue) }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun TimetableCard(entry: TimetableEntry, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = entry.courseName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(text = "${entry.startTime} - ${entry.endTime}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Venue: ${entry.venue}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimetableScreenPreview() {
    com.example.ndejjeconnect.ui.theme.NdejjeConnectTheme {
        // Preview with dummy card
        TimetableCard(
            entry = TimetableEntry(
                courseName = "Mobile Programming",
                dayOfWeek = "Monday",
                startTime = "10:00 AM",
                endTime = "1:00 PM",
                venue = "Main Lab"
            ),
            onDelete = {}
        )
    }
}
