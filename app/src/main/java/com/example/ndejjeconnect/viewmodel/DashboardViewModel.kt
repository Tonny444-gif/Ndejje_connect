package com.example.ndejjeconnect.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ndejjeconnect.data.Academia
import com.example.ndejjeconnect.data.MainRepository
import com.example.ndejjeconnect.data.local.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * DashboardViewModel manages the state and logic for the application's main dashboard.
 * It aggregates data from multiple sources including timetable, assignments, finance, and news feeds.
 */
class DashboardViewModel(private val repository: MainRepository) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val currentDay: String
        get() = SimpleDateFormat("EEEE", Locale.getDefault()).format(Calendar.getInstance().time)

    // --- Data Streams ---

    /**
     * Stream for the next scheduled class of the current day.
     */
    val nextClass: StateFlow<TimetableEntry?> = repository.getTimetableForDay(currentDay)
        .map { it.firstOrNull() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    /**
     * Stream for the count of incomplete assignments.
     */
    val pendingAssignmentsCount: StateFlow<Int> = repository.allAssignments
        .map { list -> list.count { !it.isCompleted } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    /**
     * Stream for the most immediate pending assignment.
     */
    val nextAssignment: StateFlow<Assignment?> = repository.allAssignments
        .map { list -> list.filter { !it.isCompleted }.minByOrNull { it.dueDate } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    /**
     * Stream for the user's financial status.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val financeRecord: StateFlow<FinanceRecord?> = _user.flatMapLatest { user ->
        if (user == null) flowOf(null)
        else repository.getFinanceForUser(user.regNumber)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    /**
     * Stream for the user's calculated GPA based on academic structure.
     */
    val userGpa: StateFlow<Double> = _user.map { user ->
        calculateGpaForUser(user)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    /**
     * Stream for the campus news feed.
     */
    val feedItems: StateFlow<List<FeedItem>> = repository.allFeedItems
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // --- Actions ---

    /**
     * Updates the current user context and initializes relevant dashboard data.
     */
    fun setUser(user: User?) {
        _user.value = user
        if (user != null) {
            initializeFeedData()
        }
    }

    /**
     * Adds a new highlight to the campus feed.
     */
    fun addHighlight(content: String) {
        viewModelScope.launch {
            repository.insertFeedItem(FeedItem(content = content))
        }
    }

    /**
     * Removes a specific item from the campus feed.
     */
    fun removeFeedItem(item: FeedItem) {
        viewModelScope.launch {
            repository.deleteFeedItem(item)
        }
    }

    /**
     * Updates the user's financial information.
     */
    fun updateFinance(finance: FinanceRecord) {
        viewModelScope.launch {
            repository.updateFinance(finance)
        }
    }

    // --- Internal Helpers ---

    private fun calculateGpaForUser(user: User?): Double {
        if (user == null) return 0.0
        
        val facultyEntry = Academia.academicStructure.entries.find { 
            it.value[user.level]?.containsKey(user.course) == true 
        }
        
        val units = Academia.getUnits(facultyEntry?.key, user.level, user.course)
        if (units.isEmpty()) return 0.0
        
        val seed = user.regNumber.hashCode().toLong()
        val random = Random(seed)
        
        val scores = units.map { 70 + random.nextInt(26) }
        return (scores.average() / 100) * 5.0
    }

    private fun initializeFeedData() {
        viewModelScope.launch {
            val defaults = listOf(
                "Library hours extended to 10 PM for exam season.",
                "Guest Lecture: 'Future of AI' this Friday at Main Hall.",
                "New gym equipment installed in the sports complex.",
                "Reminder: Register for Semester II modules by next Monday."
            )
            defaults.forEach { content ->
                repository.insertFeedItem(FeedItem(content = content))
            }
        }
    }
}
