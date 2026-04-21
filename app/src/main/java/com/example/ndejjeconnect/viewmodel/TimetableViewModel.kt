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
 * TimetableViewModel is the "Scheduler Brain" of the app.
 * It manages the student's weekly class schedule and helps filter it by day.
 */
class TimetableViewModel(private val repository: MainRepository) : ViewModel() {

    // 1. TRACKING CURRENT SELECTIONS
    // Stores which day of the week the student is currently looking at (defaults to Monday)
    private val _selectedDay = MutableStateFlow("Monday")
    val selectedDay: StateFlow<String> = _selectedDay.asStateFlow()

    // Stores the current user's info to know which course units they should see
    private val _user = MutableStateFlow<User?>(null)

    /**
     * Updates the current user in this "Brain" so it can fetch the right schedule.
     */
    fun setUser(user: User?) {
        _user.value = user
    }

    // 2. DATA CALCULATION
    // This list automatically updates to show only the units available for the user's specific Course/Level
    @OptIn(ExperimentalCoroutinesApi::class)
    val availableUnits: StateFlow<List<String>> = _user.map { user ->
        if (user != null) {
            // Figure out which Faculty the user is in based on their Course
            val faculty = Academia.faculties.find { f -> 
                Academia.getCourses(f, user.level).contains(user.course)
            }
            // Fetch the units for that Faculty, Level, and Course
            Academia.getUnits(faculty, user.level, user.course)
        } else {
            emptyList()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // 3. FETCHING FROM DATABASE
    // This flow automatically re-fetches classes from the database whenever the "Selected Day" changes
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

    // 4. USER ACTIONS (FUNCTIONS)

    /**
     * Changes the view to a different day (e.g., clicking 'Tuesday' on the screen).
     */
    fun selectDay(day: String) {
        _selectedDay.value = day
    }

    /**
     * Saves a new class entry to the local database on the phone.
     */
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

    /**
     * Removes a class from the schedule permanently.
     */
    fun removeEntry(id: Int) {
        viewModelScope.launch {
            repository.deleteTimetableEntry(id)
        }
    }

    /**
     * Placeholder for seeding data. Currently not used as data comes from Academia.kt.
     */
    fun seedData() {
        // No-op: Data is now pulled from Academia.kt
    }
}
