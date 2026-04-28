package com.example.ndejjeconnect.data

import com.example.ndejjeconnect.data.local.*
import kotlinx.coroutines.flow.Flow

/**
 * MainRepository acts as the central data hub for the application.
 * It abstracts access to various Room DAOs and provides a unified interface for the ViewModels.
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

    // --- Authentication & User Management ---

    suspend fun getUserByRegNumber(regNumber: String): User? = 
        userDao.getUserByRegNumber(regNumber)

    suspend fun registerUser(user: User) = 
        userDao.insertUser(user)

    suspend fun updateUser(user: User) = 
        userDao.updateUser(user)


    // --- Academic & Schedule ---

    fun getTimetableForDay(day: String): Flow<List<TimetableEntry>> = 
        timetableDao.getTimetableForDay(day)

    suspend fun insertTimetableEntry(entry: TimetableEntry) = 
        timetableDao.insertEntry(entry)

    suspend fun deleteTimetableEntry(id: Int) = 
        timetableDao.deleteEntry(id)


    // --- Assignments & Tasks ---

    val allAssignments: Flow<List<Assignment>> = 
        assignmentDao.getAllAssignments()

    suspend fun insertAssignment(assignment: Assignment) = 
        assignmentDao.insertAssignment(assignment)

    suspend fun updateAssignment(assignment: Assignment) = 
        assignmentDao.updateAssignment(assignment)


    // --- Note Taking ---

    val allNotes: Flow<List<Note>> = 
        noteDao.getAllNotes()

    suspend fun insertNote(note: Note) = 
        noteDao.insertNote(note)

    suspend fun deleteNote(note: Note) = 
        noteDao.deleteNote(note)


    // --- Financial Records ---

    fun getFinanceForUser(regNumber: String): Flow<FinanceRecord?> = 
        financeDao.getFinanceForUser(regNumber)

    suspend fun updateFinance(finance: FinanceRecord) = 
        financeDao.insertFinance(finance)


    // --- Campus News Feed ---

    val allFeedItems: Flow<List<FeedItem>> = 
        feedDao.getAllFeedItems()

    suspend fun insertFeedItem(item: FeedItem) = 
        feedDao.insertFeedItem(item)

    suspend fun deleteFeedItem(item: FeedItem) = 
        feedDao.deleteFeedItem(item)


    // --- Library Services ---

    val allBooks: Flow<List<Book>> = 
        libraryDao.getAllBooks()

    fun getBooksByCategory(category: String): Flow<List<Book>> = 
        libraryDao.getBooksByCategory(category)

    suspend fun insertBook(book: Book) = 
        libraryDao.insertBook(book)

    fun getLoansForUser(regNumber: String): Flow<List<BookLoan>> = 
        libraryDao.getLoansForUser(regNumber)

    suspend fun insertLoan(loan: BookLoan) = 
        libraryDao.insertLoan(loan)

    suspend fun updateLoan(loan: BookLoan) = 
        libraryDao.updateLoan(loan)

    val allSpaces: Flow<List<StudySpace>> = 
        libraryDao.getAllSpaces()

    fun getBookingsForUser(regNumber: String): Flow<List<SpaceBooking>> = 
        libraryDao.getBookingsForUser(regNumber)

    suspend fun insertBooking(booking: SpaceBooking) = 
        libraryDao.insertBooking(booking)

    suspend fun insertSpace(space: StudySpace) = 
        libraryDao.insertSpace(space)
}
