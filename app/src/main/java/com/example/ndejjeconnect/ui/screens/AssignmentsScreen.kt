package com.example.ndejjeconnect.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ndejjeconnect.data.local.Assignment
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.ndejjeconnect.viewmodel.AssignmentsViewModel

/**
 * View Layer: Assignments Screen
 * Centralized task tracking for students.
 */
@Composable
fun AssignmentsScreen(
    viewModel: AssignmentsViewModel,
    onEditAssignment: (Int) -> Unit
) {
    val assignments by viewModel.assignments.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Open Add Assignment */ }) {
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

@Composable
fun AssignmentCard(
    assignment: Assignment,
    onToggle: () -> Unit,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
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
                Text(text = "Due: April 25", style = MaterialTheme.typography.bodySmall)
            }
            TextButton(onClick = onEdit) {
                Text("Edit")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AssignmentsScreenPreview() {
    val mockAssignments = listOf(
        Assignment(title = "Assignment 1", description = "Desc", dueDate = 0L, priority = 1)
    )
    com.example.ndejjeconnect.ui.theme.NdejjeConnectTheme {
        Scaffold { padding ->
            Column(modifier = Modifier.padding(padding).padding(16.dp)) {
                AssignmentCard(assignment = mockAssignments[0], onToggle = {}, onEdit = {})
            }
        }
    }
}
