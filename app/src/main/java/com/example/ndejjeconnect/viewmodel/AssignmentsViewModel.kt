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
 * AssignmentsViewModel: The "Task Manager".
 * 
 * Think of this as the student's internal "To-Do List Manager". 
 * - They maintain the main list of things to do (StateFlow).
 * - They help you "Add a new sticky note" to the fridge (Add Assignment).
 * - They help you "Cross something off the list" (Toggle Completion).
 * - They help you "Correct a note" (Update Assignment).
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

    /**
     * Add Assignment: Writing a new task on a sticky note.
     */
    fun addAssignment(title: String, description: String, dueDate: Long, priority: Int) {
        viewModelScope.launch {
            val newAssignment = Assignment(
                title = title,
                description = description,
                dueDate = dueDate,
                priority = priority
            )
            repository.insertAssignment(newAssignment)
        }
    }

    /**
     * Toggle Completion: Drawing a line through a task when it's done.
     */
    fun toggleAssignmentCompletion(assignment: Assignment) {
        viewModelScope.launch {
            repository.updateAssignment(assignment.copy(isCompleted = !assignment.isCompleted))
        }
    }

    /**
     * Update Assignment: Changing the details of a task (like the due date or priority).
     */
    fun updateAssignment(assignment: Assignment) {
        viewModelScope.launch {
            repository.updateAssignment(assignment)
        }
    }
}
