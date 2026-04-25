package com.example.ndejjeconnect.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ndejjeconnect.data.MainRepository
import com.example.ndejjeconnect.data.local.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * NotesViewModel: The "Librarian".
 * 
 * Think of this as the person in charge of the university's notebook archive. 
 * - They keep a list of all notes ever written (StateFlow).
 * - They help you "File a new notebook" (Add Note).
 * - They help you "Remove an old notebook" (Delete Note).
 */
class NotesViewModel(private val repository: MainRepository) : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    init {
        // Collect notes from the repository and update the UI state
        viewModelScope.launch {
            repository.allNotes.collect { itemList ->
                _notes.value = itemList
            }
        }
    }

    /**
     * Add Note: Writing a new notebook and putting it on the shelf.
     */
    fun addNote(title: String, content: String, courseUnit: String, attachmentUri: String? = null) {
        viewModelScope.launch {
            val newNote = Note(
                title = title,
                content = content,
                courseUnit = courseUnit,
                attachmentUri = attachmentUri
            )
            repository.insertNote(newNote)
        }
    }

    /**
     * Delete Note: Taking a notebook off the shelf and throwing it away.
     */
    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }
}
