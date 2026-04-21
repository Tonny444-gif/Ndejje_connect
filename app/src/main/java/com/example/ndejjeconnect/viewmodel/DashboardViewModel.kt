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

class DashboardViewModel(private val repository: MainRepository) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    fun setUser(user: User?) {
        _user.value = user
    }

    private val today: String
        get() = SimpleDateFormat("EEEE", Locale.getDefault()).format(Calendar.getInstance().time)

    val nextClass: StateFlow<TimetableEntry?> = repository.getTimetableForDay(today)
        .map { it.firstOrNull() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val pendingAssignmentsCount: StateFlow<Int> = repository.allAssignments
        .map { list -> list.count { !it.isCompleted } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val nextAssignment: StateFlow<Assignment?> = repository.allAssignments
        .map { list -> list.filter { !it.isCompleted }.minByOrNull { it.dueDate } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val financeRecord: StateFlow<FinanceRecord?> = _user.flatMapLatest { user ->
        if (user == null) flowOf(null)
        else repository.getFinanceForUser(user.regNumber)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

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

    fun updateFinance(record: FinanceRecord) {
        viewModelScope.launch {
            repository.updateFinance(record)
        }
    }

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
}
