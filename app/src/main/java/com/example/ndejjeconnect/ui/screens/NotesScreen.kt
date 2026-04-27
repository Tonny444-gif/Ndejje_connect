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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ndejjeconnect.data.local.Note
import com.example.ndejjeconnect.viewmodel.NotesViewModel

/**
 * Notes Screen: The "Lecture Notebook".
 * 
 * Think of this screen like a digital shelf of notebooks. 
 * Every time you attend a lecture or want to remember something, you create a "Note".
 * 
 * - The "Manager" (ViewModel) handles fetching all your saved notebooks.
 * - The "Plus Button" lets you grab a fresh sheet of paper to write on.
 */
@Composable
fun NotesScreen(viewModel: NotesViewModel) {
    // Collect the state of notes from the ViewModel
    val notes by viewModel.notes.collectAsState()
    
    // UI state for showing dialogs
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedNote by remember { mutableStateOf<Note?>(null) }

    NotesContent(
        notes = notes,
        onAddClick = { showAddDialog = true },
        onNoteClick = { selectedNote = it }
    )

    if (showAddDialog) {
        AddNoteDialog(
            onDismiss = { showAddDialog = false },
            onSave = { title, content, course, uri ->
                viewModel.addNote(title, content, course, uri)
                showAddDialog = false
            }
        )
    }

    if (selectedNote != null) {
        ViewNoteDialog(
            note = selectedNote!!,
            onDismiss = { selectedNote = null },
            onDelete = {
                viewModel.deleteNote(selectedNote!!)
                selectedNote = null
            }
        )
    }
}

@Composable
fun ViewNoteDialog(
    note: Note,
    onDismiss: () -> Unit,
    onDelete: () -> Unit
) {
    // --- STEP 1: Reading the Note ---
    // This is like opening a notebook to a specific page to read what's inside.
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(note.title, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Course: ${note.courseUnit}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                HorizontalDivider()
                Text(
                    text = note.content,
                    style = MaterialTheme.typography.bodyMedium
                )
                if (note.attachmentUri != null) {
                    Text(
                        text = "Attachment available",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        },
        dismissButton = {
            // --- STEP 2: Tearing out a Page ---
            // If the user doesn't need this note anymore, they can "delete" it (burn it).
            TextButton(
                onClick = onDelete,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Delete")
            }
        }
    )
}

@Composable
fun AddNoteDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, String, String?) -> Unit
) {
    // --- STEP 1: Setting up a Blank Page ---
    // We create temporary slots (State) to hold the title, course, and text
    // while the user is typing.
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var courseUnit by remember { mutableStateOf("") }
    var attachmentUri by remember { mutableStateOf<String?>(null) }

    // This is like a "File Picker" tool that helps us find documents on the phone.
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
                // Fields for the user to fill in the "Notebook" details.
                OutlinedTextField(
                    value = title, 
                    onValueChange = { title = it }, 
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = courseUnit, 
                    onValueChange = { courseUnit = it }, 
                    label = { Text("Course Unit") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Content") },
                    modifier = Modifier.fillMaxWidth().height(120.dp)
                )
                
                // Button to "clip" a file or image to this note.
                TextButton(onClick = { launcher.launch("*/*") }) {
                    Icon(Icons.Default.AttachFile, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(if (attachmentUri == null) "Attach Document/Image" else "File Attached ✅")
                }
            }
        },
        confirmButton = {
            // --- STEP 2: Placing it on the Shelf ---
            // Once saved, the data is handed to the Brain (ViewModel) to keep it safe.
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

@Composable
fun NotesContent(
    notes: List<Note>,
    onAddClick: () -> Unit,
    onNoteClick: (Note) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            // This is the "Add New Note" button.
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

            // If the bookshelf is empty, we show a friendly message.
            if (notes.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text("No notes found. Create one!")
                }
            } else {
                // --- STEP 3: Displaying the Grid ---
                // We show all the notebooks in a neat grid (two columns).
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalItemSpacing = 8.dp
                ) {
                    items(notes) { note ->
                        NoteCard(note, onClick = { onNoteClick(note) })
                    }
                }
            }
        }
    }
}

/**
 * Note Card: A "Post-it" preview.
 * Shows the course name, title, and a few lines of the note content.
 */
@Composable
fun NoteCard(note: Note, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp), // Added small vertical padding for better spacing
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Display course unit tag
            Text(
                text = note.courseUnit,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
            // Title with ellipsis for long text
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Content preview restricted to 4 lines
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
    com.example.ndejjeconnect.ui.theme.NdejjeConnectTheme {
        NotesContent(notes = emptyList(), onAddClick = {}, onNoteClick = {})
    }
}