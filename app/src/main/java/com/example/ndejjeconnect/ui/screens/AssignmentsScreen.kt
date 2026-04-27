package com.example.ndejjeconnect.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ndejjeconnect.data.local.Assignment
import com.example.ndejjeconnect.viewmodel.AssignmentsViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * Assignments Screen: The "Task Tracker".
 * 
 * Think of this screen like a digital "To-Do List" on a fridge. 
 * It keeps track of all the homework, projects, and tasks you need to finish.
 * 
 * - The "Brain" (AssignmentsViewModel) remembers all your tasks.
 * - The "Plus Button" lets you pin a new task to the list.
 */
@Composable
fun AssignmentsScreen(
    viewModel: AssignmentsViewModel,
    onEditAssignment: (Int) -> Unit
) {
    // Observe assignments from the ViewModel as state
    val assignments by viewModel.assignments.collectAsState()
    
    // State to manage the visibility of the Add Assignment dialog
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            // This is the "Add Task" button.
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Assignment")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text(
                text = "Active Assignments",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // If you've finished everything (or haven't started), we show a clean slate.
            if (assignments.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No assignments yet. Click + to add one.")
                }
            } else {
                // --- STEP 1: Listing the Tasks ---
                // We show your tasks one by one in a scrollable list.
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(assignments) { assignment ->
                        AssignmentCard(
                            assignment = assignment,
                            onToggle = { viewModel.toggleAssignmentCompletion(assignment) },
                            onEdit = { onEditAssignment(assignment.id) }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddAssignmentDialog(
            onDismiss = { showAddDialog = false },
            onSave = { title, desc, priority ->
                // Saving the new task into the "Brain".
                viewModel.addAssignment(title, desc, System.currentTimeMillis(), priority)
                showAddDialog = false
            }
        )
    }
}

/**
 * Dialog for creating a new assignment.
 * Provides inputs for title, description, and priority level.
 */
@Composable
fun AddAssignmentDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, Int) -> Unit
) {
    // Temporary storage for what the user is typing.
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(2) } // Default Medium

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Assignment") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Setting the name of the task.
                OutlinedTextField(
                    value = title, 
                    onValueChange = { title = it }, 
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                // Writing down more details about the task.
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth().height(100.dp)
                )
                // Picking how urgent this task is.
                Text("Priority (1: High, 3: Low)", style = MaterialTheme.typography.labelSmall)
                Slider(
                    value = priority.toFloat(),
                    onValueChange = { priority = it.toInt() },
                    valueRange = 1f..3f,
                    steps = 1
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(title, description, priority) },
                enabled = title.isNotBlank()
            ) {
                Text("Add Task")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

/**
 * Assignment Card: A single "Sticky Note" for a task.
 * It shows the title, when it's due, and a checkbox to mark it as "Done".
 */
@Composable
fun AssignmentCard(
    assignment: Assignment,
    onToggle: () -> Unit,
    onEdit: () -> Unit
) {
    val dateStr = remember(assignment.dueDate) {
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(assignment.dueDate))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp), // Added small vertical padding for consistency
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp) // Subtle elevation
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = assignment.isCompleted,
                onCheckedChange = { onToggle() }
            )
            Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                Text(
                    text = assignment.title,
                    fontWeight = FontWeight.Bold,
                    textDecoration = if (assignment.isCompleted) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                )
                Text(text = "Due: $dateStr", style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Add, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AssignmentsScreenPreview() {
    val mockAssignments = listOf(
        Assignment(title = "Assignment 1", description = "Desc", dueDate = System.currentTimeMillis(), priority = 1)
    )
    com.example.ndejjeconnect.ui.theme.NdejjeConnectTheme {
        Scaffold { padding ->
            Column(modifier = Modifier.padding(padding).padding(16.dp)) {
                AssignmentCard(assignment = mockAssignments[0], onToggle = {}, onEdit = {})
            }
        }
    }
}