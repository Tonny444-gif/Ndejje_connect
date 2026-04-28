package com.example.ndejjeconnect.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ndejjeconnect.data.local.Assignment
import com.example.ndejjeconnect.viewmodel.AssignmentsViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AssignmentsScreen(
    viewModel: AssignmentsViewModel,
    onEditAssignment: (Int) -> Unit
) {
    val assignments by viewModel.assignments.collectAsState()
    var isAddAssignmentDialogOpen by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isAddAssignmentDialogOpen = true },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add New Assignment")
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                AssignmentsHeader()

                if (assignments.isEmpty()) {
                    EmptyAssignmentsView()
                } else {
                    AssignmentsList(
                        assignments = assignments,
                        onToggleCompletion = { viewModel.toggleAssignmentCompletion(it) },
                        onEditRequest = { onEditAssignment(it.id) }
                    )
                }
            }
        }
    }

    if (isAddAssignmentDialogOpen) {
        AddAssignmentDialog(
            onDismiss = { isAddAssignmentDialogOpen = false },
            onSave = { title, desc, priority ->
                viewModel.addAssignment(title, desc, System.currentTimeMillis(), priority)
                isAddAssignmentDialogOpen = false
            }
        )
    }
}

@Composable
private fun AssignmentsHeader() {
    Text(
        text = "Active Assignments",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.ExtraBold,
        modifier = Modifier.padding(bottom = 20.dp)
    )
}

@Composable
private fun EmptyAssignmentsView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No assignments yet. Tap + to begin.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
private fun AssignmentsList(
    assignments: List<Assignment>,
    onToggleCompletion: (Assignment) -> Unit,
    onEditRequest: (Assignment) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(assignments, key = { it.id }) { assignment ->
            AssignmentCard(
                assignment = assignment,
                onToggle = { onToggleCompletion(assignment) },
                onEdit = { onEditRequest(assignment) }
            )
        }
    }
}

@Composable
fun AssignmentCard(
    assignment: Assignment,
    onToggle: () -> Unit,
    onEdit: () -> Unit
) {
    val dateDisplay = remember(assignment.dueDate) {
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(assignment.dueDate))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = assignment.isCompleted,
                onCheckedChange = { onToggle() }
            )
            
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = assignment.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textDecoration = if (assignment.isCompleted) TextDecoration.LineThrough else null
                )
                Text(
                    text = "Due: $dateDisplay",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            IconButton(onClick = onEdit) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Assignment",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun AddAssignmentDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableIntStateOf(2) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Assignment") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task Title") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Additional Details") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
                
                Column {
                    Text(
                        text = "Priority Level: ${when(priority) {
                            1 -> "High"
                            2 -> "Medium"
                            else -> "Low"
                        }}",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Slider(
                        value = priority.toFloat(),
                        onValueChange = { priority = it.toInt() },
                        valueRange = 1f..3f,
                        steps = 1
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(title, description, priority) },
                enabled = title.isNotBlank()
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun AssignmentsScreenPreview() {
    com.example.ndejjeconnect.ui.theme.NdejjeConnectTheme {
        // Mock preview
    }
}
