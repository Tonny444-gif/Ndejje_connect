package com.example.ndejjeconnect.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ndejjeconnect.viewmodel.AssignmentsViewModel
import kotlinx.coroutines.flow.map

/**
 * View Layer: Edit Assignment Screen
 * Allows students to modify existing assignment details.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAssignmentScreen(
    assignmentId: Int,
    viewModel: AssignmentsViewModel,
    onNavigateBack: () -> Unit
) {
    val assignments by viewModel.assignments.collectAsState()
    val assignment = remember(assignments, assignmentId) {
        assignments.find { it.id == assignmentId }
    }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    
    // Update local state when assignment is loaded
    LaunchedEffect(assignment) {
        assignment?.let {
            title = it.title
            description = it.description
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Assignment") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Button(
                onClick = {
                    assignment?.let {
                        viewModel.updateAssignment(it.copy(title = title, description = description))
                        onNavigateBack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = title.isNotBlank()
            ) {
                Text("Save Changes")
            }
        }
    }
}
