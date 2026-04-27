package com.example.ndejjeconnect.data

import com.example.ndejjeconnect.data.local.*
import kotlinx.coroutines.flow.Flow

/**
 * Repository Layer: Acts as a single source of truth for all data.
 * Clean Architecture: abstracts data sources from the ViewModels.
 */
class MainRepository(
    private val noteDao: NoteDao,
    private val assignmentDao: AssignmentDao,
    private val timetableDao: TimetableDao,
    private val userDao: UserDao,
    private val financeDao: FinanceDao,
    private val feedDao: FeedDao,
    private val libraryDao: LibraryDao
) {
    // --- Notes Management ---
    // Provides a stream of all saved notes
    val allNotes: Flow<List<Note>> = noteDao.getAllNotes()
    suspend fun insertNote(note: Note) = noteDao.insertNote(note)
    suspend fun deleteNote(note: Note) = noteDao.deleteNote(note)

    // --- Assignments Tracking ---
    // Provides a stream of all assignments sorted by due date
    val allAssignments: Flow<List<Assignment>> = assignmentDao.getAllAssignments()
    suspend fun insertAssignment(assignment: Assignment) = assignmentDao.insertAssignment(assignment)
    suspend fun updateAssignment(assignment: Assignment) = assignmentDao.updateAssignment(assignment)

    // --- Timetable / Schedule ---
    // Fetches entries for a specific day (e.g., "Monday")
    fun getTimetableForDay(day: String): Flow<List<TimetableEntry>> = timetableDao.getTimetableForDay(day)
    suspend fun insertTimetableEntry(entry: TimetableEntry) = timetableDao.insertEntry(entry)
    suspend fun deleteTimetableEntry(id: Int) = timetableDao.deleteEntry(id)

    // --- User Profile & Authentication ---
    suspend fun getUserByRegNumber(regNumber: String) = userDao.getUserByRegNumber(regNumber)
    suspend fun registerUser(user: User) = userDao.insertUser(user)
    suspend fun updateUser(user: User) = userDao.updateUser(user)

    // --- Financial Records ---
    fun getFinanceForUser(regNumber: String): Flow<FinanceRecord?> = financeDao.getFinanceForUser(regNumber)
    suspend fun updateFinance(finance: FinanceRecord) = financeDao.insertFinance(finance)

    // --- Campus Feed ---
    val allFeedItems: Flow<List<FeedItem>> = feedDao.getAllFeedItems()
    suspend fun insertFeedItem(item: FeedItem) = feedDao.insertFeedItem(item)
    suspend fun deleteFeedItem(item: FeedItem) = feedDao.deleteFeedItem(item)

    // --- Library Services ---
    // Book Catalog
    val allBooks: Flow<List<Book>> = libraryDao.getAllBooks()
    fun getBooksByCategory(category: String): Flow<List<Book>> = libraryDao.getBooksByCategory(category)
    suspend fun insertBook(book: Book) = libraryDao.insertBook(book)

    // Loans and Bookings
    fun getLoansForUser(regNumber: String): Flow<List<BookLoan>> = libraryDao.getLoansForUser(regNumber)
    suspend fun insertLoan(loan: BookLoan) = libraryDao.insertLoan(loan)
    suspend fun updateLoan(loan: BookLoan) = libraryDao.updateLoan(loan)

    val allSpaces: Flow<List<StudySpace>> = libraryDao.getAllSpaces()
    fun getBookingsForUser(regNumber: String): Flow<List<SpaceBooking>> = libraryDao.getBookingsForUser(regNumber)
    suspend fun insertBooking(booking: SpaceBooking) = libraryDao.insertBooking(booking)

    suspend fun insertSpace(space: StudySpace) = libraryDao.insertSpace(space)
}
