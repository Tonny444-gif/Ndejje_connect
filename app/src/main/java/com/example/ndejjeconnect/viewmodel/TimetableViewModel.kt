package com.example.ndejjeconnect.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ndejjeconnect.data.MainRepository
import com.example.ndejjeconnect.data.local.TimetableEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

/**
 * Logic Layer: Manages the Timetable state.
 * Filters the schedule based on the selected day.
 */
class TimetableViewModel(private val repository: MainRepository) : ViewModel() {

    private val _selectedDay = MutableStateFlow("Monday")
    val selectedDay: StateFlow<String> = _selectedDay.asStateFlow()

    private val _timetableEntries = MutableStateFlow<List<TimetableEntry>>(emptyList())
    val timetableEntries: StateFlow<List<TimetableEntry>> = _timetableEntries.asStateFlow()

    init {
        // Automatically update the entries whenever the selected day changes
        viewModelScope.launch {
            _selectedDay.collect { day ->
                repository.getTimetableForDay(day).collect { entries ->
                    _timetableEntries.value = entries
                }
            }
        }
    }

    fun selectDay(day: String) {
        _selectedDay.value = day
    }

    fun addEntry(entry: TimetableEntry) {
        viewModelScope.launch {
            repository.insertTimetableEntry(entry)
        }
    }
}
