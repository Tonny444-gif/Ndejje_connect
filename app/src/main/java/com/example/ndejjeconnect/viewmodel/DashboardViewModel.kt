package com.example.ndejjeconnect.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ndejjeconnect.data.Academia
import com.example.ndejjeconnect.data.MainRepository
import com.example.ndejjeconnect.data.local.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * DashboardViewModel: The "Activity Coordinator".
 * 
 * Think of this as the student's personal assistant. 
 * Their job is to gather all the important information (Next Class, Tasks, Fees) 
 * and present it on the "Main Notice Board" (Dashboard Screen).
 * 
 * - They check the timetable for today's classes.
 * - They count how many homework tasks are left.
 * - They look at the "Pocket Tracker" to see fee balances.
 */
class DashboardViewModel(private val repository: MainRepository) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    fun setUser(user: User?) {
        _user.value = user
        if (user != null) {
            seedDashboardData()
        }
    }

    private fun seedDashboardData() {
        viewModelScope.launch {
            val sampleFeed = listOf(
                FeedItem(content = "Library hours extended to 10 PM for exam season."),
                FeedItem(content = "Guest Lecture: 'Future of AI' this Friday at Main Hall."),
                FeedItem(content = "New gym equipment installed in the sports complex."),
                FeedItem(content = "Reminder: Register for Semester II modules by next Monday.")
            )
            sampleFeed.forEach { repository.insertFeedItem(it) }
        }
    }

    private val today: String
        get() = SimpleDateFormat("EEEE", Locale.getDefault()).format(Calendar.getInstance().time)

    // --- TASKS FOR THE ASSISTANT ---

    /**
     * Next Class: Looking at the "Weekly Planner" to see what's next for today.
     */
    val nextClass: StateFlow<TimetableEntry?> = repository.getTimetableForDay(today)
        .map { it.firstOrNull() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    /**
     * Homework Count: Counting how many sticky notes are still on the "To-Do List".
     */
    val pendingAssignmentsCount: StateFlow<Int> = repository.allAssignments
        .map { list -> list.count { !it.isCompleted } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    /**
     * Urgent Task: Finding the homework that is due the soonest.
     */
    val nextAssignment: StateFlow<Assignment?> = repository.allAssignments
        .map { list -> list.filter { !it.isCompleted }.minByOrNull { it.dueDate } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    /**
     * Fee Balance: Checking the student's "Pocket Tracker".
     */
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val financeRecord: StateFlow<FinanceRecord?> = _user.flatMapLatest { user ->
        if (user == null) flowOf(null)
        else repository.getFinanceForUser(user.regNumber)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    /**
     * GPA Calculation: Simulating the "Report Card" based on the student's course.
     */
    val userGpa: StateFlow<Double> = _user.map { user ->
        if (user == null) return@map 0.0
        val faculty = Academia.academicStructure.entries.find { it.value[user.level]?.containsKey(user.course) == true }?.key
        val units = Academia.getUnits(faculty, user.level, user.course)
        if (units.isEmpty()) 0.0
        else {
            val random = Random(user.regNumber.hashCode().toLong())
            val scores = units.map { 70 + random.nextInt(26) }
            scores.average() / 100 * 5.0
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    /**
     * Campus Feed: Getting the latest news and highlights.
     */
    val feedItems: StateFlow<List<FeedItem>> = repository.allFeedItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addHighlight(content: String) {
        viewModelScope.launch {
            repository.insertFeedItem(FeedItem(content = content))
        }
    }

    fun removeFeedItem(item: FeedItem) {
        viewModelScope.launch {
            repository.deleteFeedItem(item)
        }
    }

    fun updateFinance(finance: FinanceRecord) {
        viewModelScope.launch {
            repository.updateFinance(finance)
        }
    }
}
