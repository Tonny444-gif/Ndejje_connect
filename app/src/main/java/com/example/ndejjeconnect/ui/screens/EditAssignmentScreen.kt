package com.example.ndejjeconnect.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ndejjeconnect.viewmodel.AssignmentsViewModel

/**
 * EditAssignmentScreen provides an interface for modifying existing assignment details.
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

    var titleText by remember { mutableStateOf("") }
    var descriptionText by remember { mutableStateOf("") }
    
    LaunchedEffect(assignment) {
        assignment?.let {
            titleText = it.title
            descriptionText = it.description
        }
    }

    Scaffold(
        topBar = {
            EditAssignmentTopBar(onBackClick = onNavigateBack)
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                EditAssignmentForm(
                    title = titleText,
                    onTitleChange = { titleText = it },
                    description = descriptionText,
                    onDescriptionChange = { descriptionText = it }
                )

                Spacer(modifier = Modifier.weight(1f))

                SaveAssignmentButton(
                    onSave = {
                        assignment?.let {
                            viewModel.updateAssignment(it.copy(title = titleText, description = descriptionText))
                            onNavigateBack()
                        }
                    },
                    isEnabled = titleText.isNotBlank()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditAssignmentTopBar(onBackClick: () -> Unit) {
    TopAppBar(
        title = { Text("Update Task") },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Discard and Return")
            }
        }
    )
}

@Composable
private fun EditAssignmentForm(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Assignment Details",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text("Task Title") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = MaterialTheme.shapes.medium
        )

        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Extended Description") },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 150.dp),
            shape = MaterialTheme.shapes.medium
        )
    }
}

@Composable
private fun SaveAssignmentButton(onSave: () -> Unit, isEnabled: Boolean) {
    Button(
        onClick = onSave,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        enabled = isEnabled,
        shape = MaterialTheme.shapes.large
    ) {
        Text(
            text = "Confirm Changes",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}
