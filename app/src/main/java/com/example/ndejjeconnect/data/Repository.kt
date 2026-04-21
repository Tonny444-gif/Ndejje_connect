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
    private val feedDao: FeedDao
) {
    // Notes logic
    val allNotes: Flow<List<Note>> = noteDao.getAllNotes()
    suspend fun insertNote(note: Note) = noteDao.insertNote(note)
    suspend fun deleteNote(note: Note) = noteDao.deleteNote(note)

    // Assignments logic
    val allAssignments: Flow<List<Assignment>> = assignmentDao.getAllAssignments()
    suspend fun insertAssignment(assignment: Assignment) = assignmentDao.insertAssignment(assignment)
    suspend fun updateAssignment(assignment: Assignment) = assignmentDao.updateAssignment(assignment)

    // Timetable logic
    fun getTimetableForDay(day: String): Flow<List<TimetableEntry>> = timetableDao.getTimetableForDay(day)
    suspend fun insertTimetableEntry(entry: TimetableEntry) = timetableDao.insertEntry(entry)
    suspend fun deleteTimetableEntry(id: Int) = timetableDao.deleteEntry(id)

    // User/Auth logic
    suspend fun getUserByRegNumber(regNumber: String) = userDao.getUserByRegNumber(regNumber)
    suspend fun registerUser(user: User) = userDao.insertUser(user)
    suspend fun updateUser(user: User) = userDao.updateUser(user)

    // Finance logic
    fun getFinanceForUser(regNumber: String): Flow<FinanceRecord?> = financeDao.getFinanceForUser(regNumber)
    suspend fun updateFinance(finance: FinanceRecord) = financeDao.insertFinance(finance)

    // Feed logic
    val allFeedItems: Flow<List<FeedItem>> = feedDao.getAllFeedItems()
    suspend fun insertFeedItem(item: FeedItem) = feedDao.insertFeedItem(item)
    suspend fun deleteFeedItem(item: FeedItem) = feedDao.deleteFeedItem(item)
}
