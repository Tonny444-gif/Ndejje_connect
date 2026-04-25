package com.example.ndejjeconnect.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note::class, Assignment::class, TimetableEntry::class, User::class, FinanceRecord::class, FeedItem::class, Book::class, StudySpace::class, BookLoan::class, SpaceBooking::class], version = 6, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun assignmentDao(): AssignmentDao
    abstract fun timetableDao(): TimetableDao
    abstract fun userDao(): UserDao
    abstract fun financeDao(): FinanceDao
    abstract fun feedDao(): FeedDao
    abstract fun libraryDao(): LibraryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ndejje_connect_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
