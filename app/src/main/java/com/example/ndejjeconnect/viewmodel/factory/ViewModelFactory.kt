package com.example.ndejjeconnect.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ndejjeconnect.data.MainRepository
import com.example.ndejjeconnect.viewmodel.*

/**
 * ViewModelFactory is responsible for instantiating ViewModels with their required dependencies.
 * This implementation uses the standard ViewModelProvider.Factory pattern to inject the MainRepository.
 */
class ViewModelFactory(private val repository: MainRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> {
                DashboardViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AssignmentsViewModel::class.java) -> {
                AssignmentsViewModel(repository) as T
            }
            modelClass.isAssignableFrom(TimetableViewModel::class.java) -> {
                TimetableViewModel(repository) as T
            }
            modelClass.isAssignableFrom(NotesViewModel::class.java) -> {
                NotesViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LibraryViewModel::class.java) -> {
                LibraryViewModel(repository) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}
