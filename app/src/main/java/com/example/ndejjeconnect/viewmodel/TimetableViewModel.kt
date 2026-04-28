package com.example.ndejjeconnect.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ndejjeconnect.data.Academia
import com.example.ndejjeconnect.data.MainRepository
import com.example.ndejjeconnect.data.local.TimetableEntry
import com.example.ndejjeconnect.data.local.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * TimetableViewModel manages the student's academic schedule, including day selection,
 * entry management, and course unit filtering.
 */
class TimetableViewModel(private val repository: MainRepository) : ViewModel() {

    private val _selectedDay = MutableStateFlow("Monday")
    val selectedDay: StateFlow<String> = _selectedDay.asStateFlow()

    private val _user = MutableStateFlow<User?>(null)

    /**
     * Stream of course units available based on the current user's course and level.
     */
    val availableUnits: StateFlow<List<String>> = _user.map { user ->
        if (user == null) {
            emptyList()
        } else {
            val faculty = Academia.faculties.find { f -> 
                Academia.getCourses(f, user.level).contains(user.course)
            }
            Academia.getUnits(faculty, user.level, user.course)
        }
    }.stateIn(
        scope = viewModelScope, 
        started = SharingStarted.WhileSubscribed(5000), 
        initialValue = emptyList()
    )

    /**
     * Stream of timetable entries for the currently selected day.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val timetableEntries: StateFlow<List<TimetableEntry>> = _selectedDay
        .flatMapLatest { day ->
            repository.getTimetableForDay(day)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // --- Actions ---

    /**
     * Sets the current user context.
     */
    fun setUser(user: User?) {
        _user.value = user
    }

    /**
     * Updates the active day for schedule viewing.
     */
    fun selectDay(day: String) {
        _selectedDay.value = day
    }

    /**
     * persists a new class entry into the student's timetable.
     */
    fun addEntry(
        courseName: String, 
        day: String, 
        startTime: String, 
        endTime: String, 
        venue: String
    ) {
        viewModelScope.launch {
            val entry = TimetableEntry(
                courseName = courseName,
                dayOfWeek = day,
                startTime = startTime,
                endTime = endTime,
                venue = venue
            )
            repository.insertTimetableEntry(entry)
        }
    }

    /**
     * Deletes a specific entry from the timetable.
     */
    fun removeEntry(id: Int) {
        viewModelScope.launch {
            repository.deleteTimetableEntry(id)
        }
    }
}
