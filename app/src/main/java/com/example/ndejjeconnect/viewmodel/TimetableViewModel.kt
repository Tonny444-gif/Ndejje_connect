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
 * Logic Layer: Manages the Timetable state.
 * Filters the schedule based on the selected day.
 */
class TimetableViewModel(private val repository: MainRepository) : ViewModel() {

    private val _selectedDay = MutableStateFlow("Monday")
    val selectedDay: StateFlow<String> = _selectedDay.asStateFlow()

    private val _user = MutableStateFlow<User?>(null)

    fun setUser(user: User?) {
        _user.value = user
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val availableUnits: StateFlow<List<String>> = _user.map { user ->
        if (user != null) {
            // Find which faculty the user belongs to by searching the academic structure
            val faculty = Academia.faculties.find { f -> 
                Academia.getCourses(f, user.level).contains(user.course)
            }
            Academia.getUnits(faculty, user.level, user.course)
        } else {
            emptyList()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

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

    fun selectDay(day: String) {
        _selectedDay.value = day
    }

    fun addEntry(courseName: String, day: String, startTime: String, endTime: String, venue: String) {
        viewModelScope.launch {
            repository.insertTimetableEntry(
                TimetableEntry(
                    courseName = courseName,
                    dayOfWeek = day,
                    startTime = startTime,
                    endTime = endTime,
                    venue = venue
                )
            )
        }
    }

    fun removeEntry(id: Int) {
        viewModelScope.launch {
            repository.deleteTimetableEntry(id)
        }
    }

    /**
     * Seeds the database with Course Units for various programs.
     * Deprecated: Metadata is now centralized in Academia.kt
     */
    fun seedData() {
        // No-op: Data is now pulled from Academia.kt
    }
}
