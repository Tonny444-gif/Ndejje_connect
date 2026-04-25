package com.example.ndejjeconnect.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ndejjeconnect.data.MainRepository
import com.example.ndejjeconnect.viewmodel.*

/**
 * Factory Layer: Handles the instantiation of ViewModels with custom dependencies (Repository).
 */
class ViewModelFactory(private val repository: MainRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(NotesViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                NotesViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                AuthViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AssignmentsViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                AssignmentsViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                DashboardViewModel(repository) as T
            }
            modelClass.isAssignableFrom(TimetableViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                TimetableViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LibraryViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                LibraryViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
