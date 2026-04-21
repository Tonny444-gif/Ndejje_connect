package com.example.ndejjeconnect.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ndejjeconnect.data.MainRepository
import com.example.ndejjeconnect.data.Academia
import com.example.ndejjeconnect.data.local.Assignment
import com.example.ndejjeconnect.data.local.TimetableEntry
import com.example.ndejjeconnect.data.local.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Logic Layer: Dashboard ViewModel.
 * Aggregates data from multiple sources (Assignments, Timetable) to provide a summary view.
 */
class DashboardViewModel(private val repository: MainRepository) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)

    fun setUser(user: User?) {
        _user.value = user
    }

    // Observe GPA based on user's course units (matching ResultsScreen logic)
    val userGpa: StateFlow<Double> = _user.map { user ->
        if (user == null) return@map 0.0
        
        val faculty = Academia.academicStructure.entries.find { facultyEntry ->
            facultyEntry.value[user.level]?.containsKey(user.course) == true
        }?.key
        val units = Academia.getUnits(faculty, user.level, user.course)
        
        if (units.isEmpty()) 0.0
        else {
            // Consistent with ResultsScreen's random score generation for demo
            // In a real app, this would come from a repository
            val seed = user.regNumber.hashCode()
            val random = java.util.Random(seed.toLong())
            val scores = units.map { 70 + random.nextInt(26) }
            val average = scores.average()
            (average / 100 * 5.0)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // Get today's name (e.g., "Monday") using Calendar for API 24 compatibility
    private val today: String
        get() {
            val calendar = Calendar.getInstance()
            return SimpleDateFormat("EEEE", Locale.getDefault()).format(calendar.time)
        }

    // Observe the next class for today
    val nextClass: StateFlow<TimetableEntry?> = repository.getTimetableForDay(today)
        .map { it.firstOrNull() } // For simplicity, just show the first one
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Observe the count of pending assignments
    val pendingAssignmentsCount: StateFlow<Int> = repository.allAssignments
        .map { list -> list.count { !it.isCompleted } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Observe the next due assignment
    val nextAssignment: StateFlow<Assignment?> = repository.allAssignments
        .map { list -> list.filter { !it.isCompleted }.minByOrNull { it.dueDate } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}
