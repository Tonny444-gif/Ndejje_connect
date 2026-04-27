package com.example.ndejjeconnect.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    /**
     * Retrieves all notes from the database, ordered by creation date.
     */
    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}

@Dao
interface AssignmentDao {
    /**
     * Retrieves all assignments, sorted by the soonest due date.
     */
    @Query("SELECT * FROM assignments ORDER BY dueDate ASC")
    fun getAllAssignments(): Flow<List<Assignment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssignment(assignment: Assignment)

    @Update
    suspend fun updateAssignment(assignment: Assignment)

    @Delete
    suspend fun deleteAssignment(assignment: Assignment)
}

@Dao
interface TimetableDao {
    /**
     * Retrieves the schedule for a specific day.
     */
    @Query("SELECT * FROM timetable WHERE dayOfWeek = :day")
    fun getTimetableForDay(day: String): Flow<List<TimetableEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: TimetableEntry)

    @Query("DELETE FROM timetable WHERE id = :id")
    suspend fun deleteEntry(id: Int)
}


@Dao
interface UserDao {
    /**
     * Finds a user by their registration number.
     */
    @Query("SELECT * FROM users WHERE regNumber = :regNumber LIMIT 1")
    suspend fun getUserByRegNumber(regNumber: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)
}

@Dao
interface FinanceDao {
    /**
     * Provides financial status for a specific user.
     */
    @Query("SELECT * FROM finance WHERE regNumber = :regNumber")
    fun getFinanceForUser(regNumber: String): Flow<FinanceRecord?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFinance(finance: FinanceRecord)
}

@Dao
interface FeedDao {
    /**
     * Retrieves campus feed items, newest first.
     */
    @Query("SELECT * FROM feed_items ORDER BY timestamp DESC")
    fun getAllFeedItems(): Flow<List<FeedItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeedItem(item: FeedItem)

    @Delete
    suspend fun deleteFeedItem(item: FeedItem)
}

@Dao
interface LibraryDao {
    // --- Books ---
    @Query("SELECT * FROM books")
    fun getAllBooks(): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE category = :category")
    fun getBooksByCategory(category: String): Flow<List<Book>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book)

    // --- Loans ---
    @Query("SELECT * FROM book_loans WHERE regNumber = :regNumber")
    fun getLoansForUser(regNumber: String): Flow<List<BookLoan>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLoan(loan: BookLoan)

    @Update
    suspend fun updateLoan(loan: BookLoan)

    // --- Study Spaces ---
    @Query("SELECT * FROM study_spaces")
    fun getAllSpaces(): Flow<List<StudySpace>>

    @Query("SELECT * FROM space_bookings WHERE regNumber = :regNumber")
    fun getBookingsForUser(regNumber: String): Flow<List<SpaceBooking>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: SpaceBooking)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpace(space: StudySpace)
}


