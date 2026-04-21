package com.example.ndejjeconnect.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ndejjeconnect.data.local.Note
import com.example.ndejjeconnect.viewmodel.NotesViewModel

/**
 * View Layer: Notes Screen
 * Central repository for course notes.
 * Media-Ready: Includes functionality to attach system files/images.
 */
@Composable
fun NotesScreen(viewModel: NotesViewModel) {
    val notes by viewModel.notes.collectAsState()
    val availableCourses by viewModel.availableCourses.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    NotesContent(
        notes = notes,
        onAddClick = { showAddDialog = true }
    )

    if (showAddDialog) {
        AddNoteDialog(
            availableCourses = availableCourses,
            onDismiss = { showAddDialog = false },
            onSave = { title, content, course, uri ->
                viewModel.addNote(title, content, course, uri)
                showAddDialog = false
            }
        )
    }
}

/**
 * Dialog for adding a new note with optional attachment.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteDialog(
    availableCourses: List<String>,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var courseUnit by remember { mutableStateOf(availableCourses.firstOrNull() ?: "") }
    var attachmentUri by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        attachmentUri = uri?.toString()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Course Note") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = courseUnit,
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
                        availableCourses.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption) },
                                onClick = {
                                    courseUnit = selectionOption
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Content") },
                    modifier = Modifier.fillMaxWidth().height(120.dp)
                )
                
                TextButton(onClick = { launcher.launch("*/*") }) {
                    Icon(Icons.Default.AttachFile, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(if (attachmentUri == null) "Attach Document/Image" else "File Attached ✅")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(title, content, courseUnit, attachmentUri) },
                enabled = title.isNotBlank() && content.isNotBlank()
            ) {
                Text("Save Note")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

/**
 * Stateless UI for the Notes Screen.
 */
@Composable
fun NotesContent(
    notes: List<Note>,
    onAddClick: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text(
                text = "Course Notes",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalItemSpacing = 8.dp
            ) {
                items(notes) { note ->
                    NoteCard(note)
                }
            }
        }
    }
}

@Composable
fun NoteCard(note: Note) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = note.courseUnit,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = note.content,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotesScreenPreview() {
    val mockNotes = listOf(
        Note(title = "Room Intro", content = "Room is a persistence library...", courseUnit = "Mobile Dev"),
        Note(title = "MVVM Basics", content = "Model-View-ViewModel structure...", courseUnit = "Soft Eng"),
        Note(title = "Compose Layouts", content = "Rows, Columns, and Boxes...", courseUnit = "UI Design")
    )
    com.example.ndejjeconnect.ui.theme.NdejjeConnectTheme {
        NotesContent(notes = mockNotes, onAddClick = {})
    }
}
