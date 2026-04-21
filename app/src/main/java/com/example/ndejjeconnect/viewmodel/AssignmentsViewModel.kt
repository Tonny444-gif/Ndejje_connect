package com.example.ndejjeconnect.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ndejjeconnect.data.MainRepository
import com.example.ndejjeconnect.data.local.Assignment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Logic Layer: Manages the state of the Assignments Screen.
 * Observes the repository for real-time task updates.
 */
class AssignmentsViewModel(private val repository: MainRepository) : ViewModel() {

    private val _assignments = MutableStateFlow<List<Assignment>>(emptyList())
    val assignments: StateFlow<List<Assignment>> = _assignments.asStateFlow()

    init {
        viewModelScope.launch {
            repository.allAssignments.collect { list ->
                _assignments.value = list
            }
        }
    }

    fun addAssignment(title: String, description: String, courseUnit: String, dueDate: Long, priority: Int) {
        viewModelScope.launch {
            val newAssignment = Assignment(
                title = title,
                description = description,
                courseUnit = courseUnit,
                dueDate = dueDate,
                priority = priority
            )
            repository.insertAssignment(newAssignment)
        }
    }

    fun toggleAssignmentCompletion(assignment: Assignment) {
        viewModelScope.launch {
            repository.updateAssignment(assignment.copy(isCompleted = !assignment.isCompleted))
        }
    }

    fun updateAssignment(assignment: Assignment) {
        viewModelScope.launch {
            repository.updateAssignment(assignment)
        }
    }
}
