package com.example.ndejjeconnect.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ndejjeconnect.data.MainRepository
import com.example.ndejjeconnect.viewmodel.NotesViewModel

/**
 * Factory Layer: Handles the instantiation of ViewModels with custom dependencies (Repository).
 */
class ViewModelFactory(private val repository: MainRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
