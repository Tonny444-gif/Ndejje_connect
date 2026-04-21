package com.example.ndejjeconnect.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}

@Dao
interface AssignmentDao {
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
    @Query("SELECT * FROM timetable WHERE dayOfWeek = :day")
    fun getTimetableForDay(day: String): Flow<List<TimetableEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: TimetableEntry)

    @Query("DELETE FROM timetable WHERE id = :id")
    suspend fun deleteEntry(id: Int)
}


@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE regNumber = :regNumber LIMIT 1")
    suspend fun getUserByRegNumber(regNumber: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)
}

@Dao
interface FinanceDao {
    @Query("SELECT * FROM finance WHERE regNumber = :regNumber")
    fun getFinanceForUser(regNumber: String): Flow<FinanceRecord?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFinance(finance: FinanceRecord)
}

@Dao
interface FeedDao {
    @Query("SELECT * FROM feed_items ORDER BY timestamp DESC")
    fun getAllFeedItems(): Flow<List<FeedItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeedItem(item: FeedItem)

    @Delete
    suspend fun deleteFeedItem(item: FeedItem)
}

