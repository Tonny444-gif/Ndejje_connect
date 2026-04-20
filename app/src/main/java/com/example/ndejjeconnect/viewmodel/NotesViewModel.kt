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
 * Logic Layer: Manages the state of the Notes Screen.
 * Exposes data via StateFlow for the Compose UI to observe.
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

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }
}
