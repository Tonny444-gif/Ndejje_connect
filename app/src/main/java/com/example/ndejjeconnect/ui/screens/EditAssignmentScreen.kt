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
 * Edit Assignment Screen: The "Correction Tape".
 * 
 * Sometimes we make a mistake when writing down a task or the deadline changes.
 * This screen lets you "edit" a task you already added to your list.
 * 
 * It's like pulling a sticky note off the fridge, erasing the old info, 
 * writing the new info, and sticking it back on.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAssignmentScreen(
    assignmentId: Int,
    viewModel: AssignmentsViewModel,
    onNavigateBack: () -> Unit
) {
    // --- STEP 1: Finding the Task ---
    // We look through all assignments to find the one that matches the ID we were given.
    val assignments by viewModel.assignments.collectAsState()
    val assignment = remember(assignments, assignmentId) {
        assignments.find { it.id == assignmentId }
    }

    // Temporary storage for the edits.
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    
    // Update local state when assignment is loaded from the database.
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
                    // Back button to go back to the list without saving.
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
            // Edit the Name of the Task.
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            // Edit the Details of the Task.
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            // --- STEP 2: Saving the Correction ---
            // When clicked, we tell the Brain (ViewModel) to update the info.
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
