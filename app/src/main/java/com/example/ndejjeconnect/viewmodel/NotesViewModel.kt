package com.example.ndejjeconnect.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ndejjeconnect.data.MainRepository
import com.example.ndejjeconnect.data.local.Note
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * NotesViewModel manages the state and business logic for personal course notes.
 * It provides streams of notes from the repository and handles creation and deletion.
 */
class NotesViewModel(private val repository: MainRepository) : ViewModel() {

    /**
     * StateFlow representing the current list of notes, gathered from the persistent repository.
     */
    val notes: StateFlow<List<Note>> = repository.allNotes
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * persists a new note to the repository.
     *
     * @param title The title of the note.
     * @param content The main body text of the note.
     * @param courseUnit The specific academic unit this note relates to.
     * @param attachmentUri Optional URI for a file or image attachment.
     */
    fun addNote(
        title: String, 
        content: String, 
        courseUnit: String, 
        attachmentUri: String? = null
    ) {
        viewModelScope.launch {
            val note = Note(
                title = title,
                content = content,
                courseUnit = courseUnit,
                attachmentUri = attachmentUri
            )
            repository.insertNote(note)
        }
    }

    /**
     * Removes an existing note from the repository.
     *
     * @param note The note instance to be deleted.
     */
    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }
}
