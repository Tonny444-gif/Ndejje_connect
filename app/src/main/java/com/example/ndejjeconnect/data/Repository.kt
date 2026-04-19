package com.example.ndejjeconnect.data

import com.example.ndejjeconnect.data.local.Assignment
import com.example.ndejjeconnect.data.local.AssignmentDao
import com.example.ndejjeconnect.data.local.Note
import com.example.ndejjeconnect.data.local.NoteDao
import com.example.ndejjeconnect.data.local.TimetableDao
import com.example.ndejjeconnect.data.local.TimetableEntry
import kotlinx.coroutines.flow.Flow

/**
 * Repository Layer: Acts as a single source of truth for all data.
 * Clean Architecture: abstracts data sources from the ViewModels.
 */
class MainRepository(
    private val noteDao: NoteDao,
    private val assignmentDao: AssignmentDao,
    private val timetableDao: TimetableDao
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
}
