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

/**
 * View Layer: Timetable Screen
 * Displays the lecture schedule with daily tabs.
 */
@Composable
fun TimetableScreen() {
    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            edgePadding = 16.dp,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            days.forEachIndexed { index, day ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(day) }
                )
            }
        }

        // Mock data for the blueprint
        val mockEntries = listOf(
            TimetableEntry(courseName = "Mobile Programming", dayOfWeek = "Mon", startTime = "10:00 AM", endTime = "1:00 PM", venue = "Main Campus Lab"),
            TimetableEntry(courseName = "Data Structures", dayOfWeek = "Mon", startTime = "2:00 PM", endTime = "4:00 PM", venue = "Room 4")
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(mockEntries) { entry ->
                TimetableCard(entry)
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
        TimetableScreen()
    }
}
