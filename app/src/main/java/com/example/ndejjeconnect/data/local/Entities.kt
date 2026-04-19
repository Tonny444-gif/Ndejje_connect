package com.example.ndejjeconnect.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a Course Note in the NdejjeConnect app.
 * Model Layer: Local Persistence for offline access.
 */
@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val courseUnit: String,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Represents an Academic Assignment.
 * Model Layer: Used for tracking deadlines.
 */
@Entity(tableName = "assignments")
data class Assignment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val dueDate: Long,
    val priority: Int, // 1: High, 2: Medium, 3: Low
    val isCompleted: Boolean = false
)

/**
 * Represents a Timetable entry (Lecture Schedule).
 * Model Layer: Stores course timing and venue.
 */
@Entity(tableName = "timetable")
data class TimetableEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val courseName: String,
    val dayOfWeek: String, // e.g., "Monday"
    val startTime: String, // e.g., "10:00 AM"
    val endTime: String,   // e.g., "01:00 PM"
    val venue: String
)
