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

@Composable
fun TimetableScreen(viewModel: TimetableViewModel, authViewModel: AuthViewModel) {
    val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    val selectedDay by viewModel.selectedDay.collectAsState()
    val entries by viewModel.timetableEntries.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()
    val availableUnits by viewModel.availableUnits.collectAsState()

    var isAddClassModalVisible by remember { mutableStateOf(false) }

    LaunchedEffect(currentUser) {
        viewModel.setUser(currentUser)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isAddClassModalVisible = true },
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add New Class")
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                DaySelector(
                    days = days,
                    selectedDay = selectedDay,
                    onDaySelected = { viewModel.selectDay(it) }
                )

                if (entries.isEmpty()) {
                    EmptyTimetableView(selectedDay = selectedDay)
                } else {
                    TimetableList(
                        entries = entries,
                        onDeleteEntry = { viewModel.removeEntry(it) }
                    )
                }
            }
        }
    }

    if (isAddClassModalVisible) {
        AddTimetableEntryDialog(
            availableUnits = availableUnits,
            selectedDay = selectedDay,
            onDismiss = { isAddClassModalVisible = false },
            onSave = { name, start, end, venue ->
                viewModel.addEntry(name, selectedDay, start, end, venue)
                isAddClassModalVisible = false
            }
        )
    }
}

@Composable
private fun DaySelector(
    days: List<String>,
    selectedDay: String,
    onDaySelected: (String) -> Unit
) {
    val selectedTabIndex = days.indexOf(selectedDay).coerceAtLeast(0)
    
    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        edgePadding = 16.dp,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary,
        divider = {}
    ) {
        days.forEach { day ->
            Tab(
                selected = selectedDay == day,
                onClick = { onDaySelected(day) },
                text = { 
                    Text(
                        text = day.take(3),
                        style = MaterialTheme.typography.labelLarge
                    ) 
                }
            )
        }
    }
}

@Composable
private fun EmptyTimetableView(selectedDay: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No classes for $selectedDay",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun TimetableList(
    entries: List<TimetableEntry>,
    onDeleteEntry: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(entries, key = { it.id }) { entry ->
            TimetableCard(
                entry = entry,
                onDelete = { onDeleteEntry(entry.id) }
            )
        }
    }
}

@Composable
fun TimetableCard(entry: TimetableEntry, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.courseName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${entry.startTime} - ${entry.endTime}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = " • ",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = entry.venue,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove Entry",
                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
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
        title = { Text("Add Class ($selectedDay)") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedUnitName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Course Unit") },
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

                OutlinedTextField(
                    value = startTime,
                    onValueChange = { startTime = it },
                    label = { Text("Start") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = endTime,
                    onValueChange = { endTime = it },
                    label = { Text("End") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = venue,
                    onValueChange = { venue = it },
                    label = { Text("Room / Venue") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSave(selectedUnitName, startTime, endTime, venue) }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun TimetableScreenPreview() {
    com.example.ndejjeconnect.ui.theme.NdejjeConnectTheme {
        // Mock preview
    }
}
