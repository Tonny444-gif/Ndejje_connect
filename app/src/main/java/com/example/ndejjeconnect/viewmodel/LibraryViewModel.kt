package com.example.ndejjeconnect.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ndejjeconnect.data.MainRepository
import com.example.ndejjeconnect.data.local.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LibraryViewModel(private val repository: MainRepository) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    
    fun setUser(user: User?) {
        _currentUser.value = user
        if (user != null) {
            seedSampleData()
        }
    }

    private fun seedSampleData() {
        viewModelScope.launch {
            // Seed Books
            val sampleBooks = listOf(
                Book(title = "Android Programming: The Big Nerd Ranch Guide", author = "Bill Phillips", category = "Computing", isDigital = true),
                Book(title = "Clean Architecture", author = "Robert C. Martin", category = "Software Engineering", isDigital = false, floor = "2nd Floor", aisle = "A1", shelf = "S3"),
                Book(title = "The Pragmatic Programmer", author = "Andrew Hunt", category = "Computing", isDigital = true),
                Book(title = "Modern Operating Systems", author = "Andrew S. Tanenbaum", category = "Computer Science", isDigital = false, floor = "1st Floor", aisle = "B4", shelf = "S7"),
                Book(title = "Design Patterns: Elements of Reusable Object-Oriented Software", author = "Erich Gamma", category = "Software Engineering", isDigital = false, floor = "2nd Floor", aisle = "A2", shelf = "S1")
            )
            sampleBooks.forEach { repository.insertBook(it) }

            // Seed Study Spaces
            val sampleSpaces = listOf(
                StudySpace(name = "Quiet Pod A", type = "Pod", capacity = 1),
                StudySpace(name = "Group Room 1", type = "Group Room", capacity = 6),
                StudySpace(name = "Individual Desk 12", type = "Desk", capacity = 1),
                StudySpace(name = "Media Room", type = "Group Room", capacity = 4),
                StudySpace(name = "Quiet Pod B", type = "Pod", capacity = 1)
            )
            sampleSpaces.forEach { repository.insertSpace(it) }
        }
    }

    // Books
    val allBooks = repository.allBooks.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val filteredBooks = combine(allBooks, _searchQuery) { books, query ->
        if (query.isBlank()) books
        else books.filter { 
            it.title.contains(query, ignoreCase = true) || 
            it.author.contains(query, ignoreCase = true) 
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // User Loans & Bookings
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val userLoans = _currentUser.flatMapLatest { user ->
        if (user == null) flowOf(emptyList())
        else repository.getLoansForUser(user.regNumber)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val userBookings = _currentUser.flatMapLatest { user ->
        if (user == null) flowOf(emptyList())
        else repository.getBookingsForUser(user.regNumber)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Spaces
    val allSpaces = repository.allSpaces.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    // Actions
    fun reserveBook(book: Book) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val loan = BookLoan(
                regNumber = user.regNumber,
                bookId = book.id,
                dueDate = System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000), // 7 days
                status = "Reserved"
            )
            repository.insertLoan(loan)
        }
    }

    fun bookSpace(space: StudySpace, startTime: Long, endTime: Long) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val booking = SpaceBooking(
                regNumber = user.regNumber,
                spaceId = space.id,
                startTime = startTime,
                endTime = endTime
            )
            repository.insertBooking(booking)
        }
    }
}
