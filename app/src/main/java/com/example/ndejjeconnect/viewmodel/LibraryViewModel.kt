package com.example.ndejjeconnect.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ndejjeconnect.data.MainRepository
import com.example.ndejjeconnect.data.local.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * LibraryViewModel handles library-related operations, including catalog searches,
 * book reservations, and study space bookings.
 */
class LibraryViewModel(private val repository: MainRepository) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    private val _searchQuery = MutableStateFlow("")

    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    /**
     * Stream of all available books in the collection.
     */
    val allBooks: StateFlow<List<Book>> = repository.allBooks
        .stateIn(
            scope = viewModelScope, 
            started = SharingStarted.WhileSubscribed(5000), 
            initialValue = emptyList()
        )

    /**
     * Stream of books filtered by the current search query.
     */
    val filteredBooks: StateFlow<List<Book>> = combine(allBooks, _searchQuery) { books, query ->
        if (query.isBlank()) {
            books
        } else {
            books.filter { 
                it.title.contains(query, ignoreCase = true) || 
                it.author.contains(query, ignoreCase = true) 
            }
        }
    }.stateIn(
        scope = viewModelScope, 
        started = SharingStarted.WhileSubscribed(5000), 
        initialValue = emptyList()
    )

    /**
     * Stream of all manageable study spaces.
     */
    val allSpaces: StateFlow<List<StudySpace>> = repository.allSpaces
        .stateIn(
            scope = viewModelScope, 
            started = SharingStarted.WhileSubscribed(5000), 
            initialValue = emptyList()
        )

    /**
     * Stream of library items currently on loan to the authenticated user.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val userLoans: StateFlow<List<BookLoan>> = _currentUser.flatMapLatest { user ->
        if (user == null) flowOf(emptyList())
        else repository.getLoansForUser(user.regNumber)
    }.stateIn(
        scope = viewModelScope, 
        started = SharingStarted.WhileSubscribed(5000), 
        initialValue = emptyList()
    )

    /**
     * Stream of upcoming study space bookings for the authenticated user.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val userBookings: StateFlow<List<SpaceBooking>> = _currentUser.flatMapLatest { user ->
        if (user == null) flowOf(emptyList())
        else repository.getBookingsForUser(user.regNumber)
    }.stateIn(
        scope = viewModelScope, 
        started = SharingStarted.WhileSubscribed(5000), 
        initialValue = emptyList()
    )

    // --- Actions ---

    /**
     * Configures the user context and initializes default library data.
     */
    fun setUser(user: User?) {
        _currentUser.value = user
        if (user != null) {
            initializeLibraryDefaults()
        }
    }

    /**
     * Updates the active search query for the book catalog.
     */
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    /**
     * Reserves a specific book for the current user.
     */
    fun reserveBook(book: Book) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val loan = BookLoan(
                regNumber = user.regNumber,
                bookId = book.id,
                dueDate = System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000),
                status = "Reserved"
            )
            repository.insertLoan(loan)
        }
    }

    /**
     * Schedules a study space booking for the current user.
     */
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

    // --- Internal Helpers ---

    private fun initializeLibraryDefaults() {
        viewModelScope.launch {
            // Seed Books if necessary
            val defaults = listOf(
                Book(title = "Android Programming: The Big Nerd Ranch Guide", author = "Bill Phillips", category = "Computing", isDigital = true),
                Book(title = "Clean Architecture", author = "Robert C. Martin", category = "Software Engineering", isDigital = false, floor = "2nd Floor", aisle = "A1", shelf = "S3"),
                Book(title = "The Pragmatic Programmer", author = "Andrew Hunt", category = "Computing", isDigital = true),
                Book(title = "Modern Operating Systems", author = "Andrew S. Tanenbaum", category = "Computer Science", isDigital = false, floor = "1st Floor", aisle = "B4", shelf = "S7"),
                Book(title = "Design Patterns: Elements of Reusable Object-Oriented Software", author = "Erich Gamma", category = "Software Engineering", isDigital = false, floor = "2nd Floor", aisle = "A2", shelf = "S1")
            )
            defaults.forEach { repository.insertBook(it) }

            // Seed Spaces if necessary
            val spaces = listOf(
                StudySpace(name = "Quiet Pod A", type = "Pod", capacity = 1),
                StudySpace(name = "Group Room 1", type = "Group Room", capacity = 6),
                StudySpace(name = "Individual Desk 12", type = "Desk", capacity = 1),
                StudySpace(name = "Media Room", type = "Group Room", capacity = 4),
                StudySpace(name = "Quiet Pod B", type = "Pod", capacity = 1)
            )
            spaces.forEach { repository.insertSpace(it) }
        }
    }
}
