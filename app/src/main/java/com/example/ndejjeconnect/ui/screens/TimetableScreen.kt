package com.example.ndejjeconnect.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ndejjeconnect.data.local.TimetableEntry
import com.example.ndejjeconnect.viewmodel.TimetableViewModel

/**
 * View Layer: Timetable Screen
 * Displays the lecture schedule with daily tabs.
 * MVVM: Observes TimetableViewModel for schedule data.
 */
@Composable
fun TimetableScreen(viewModel: TimetableViewModel) {
    val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    val selectedDay by viewModel.selectedDay.collectAsState()
    val entries by viewModel.timetableEntries.collectAsState()

    val selectedTabIndex = days.indexOf(selectedDay).coerceAtLeast(0)

    Column(modifier = Modifier.fillMaxSize()) {
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            edgePadding = 16.dp,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            days.forEach { day ->
                Tab(
                    selected = selectedDay == day,
                    onClick = { viewModel.selectDay(day) },
                    text = { Text(day.take(3)) } // Show "Mon", "Tue", etc.
                )
            }
        }

        if (entries.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text(text = "No classes scheduled for $selectedDay", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(entries) { entry ->
                    TimetableCard(entry)
                }
            }
        }
    }
}

@Composable
fun TimetableCard(entry: TimetableEntry) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = entry.courseName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(text = "${entry.startTime} - ${entry.endTime}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Venue: ${entry.venue}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimetableScreenPreview() {
    com.example.ndejjeconnect.ui.theme.NdejjeConnectTheme {
        // Preview with dummy card
        TimetableCard(
            entry = TimetableEntry(
                courseName = "Mobile Programming",
                dayOfWeek = "Monday",
                startTime = "10:00 AM",
                endTime = "1:00 PM",
                venue = "Main Lab"
            )
        )
    }
}
