package com.example.ndejjeconnect.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.ndejjeconnect.data.local.Assignment
import com.example.ndejjeconnect.data.local.TimetableEntry
import com.example.ndejjeconnect.viewmodel.AuthViewModel
import com.example.ndejjeconnect.viewmodel.DashboardViewModel

/**
 * View Layer: The Hub (Dashboard Screen)
 * Provides students with a high-level overview of their academic day.
 */
@Composable
fun DashboardScreen(
    authViewModel: AuthViewModel,
    dashboardViewModel: DashboardViewModel,
    onNavigateToTimetable: () -> Unit,
    onNavigateToAssignments: () -> Unit
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val nextClass by dashboardViewModel.nextClass.collectAsState()
    val nextAssignment by dashboardViewModel.nextAssignment.collectAsState()
    val pendingCount by dashboardViewModel.pendingAssignmentsCount.collectAsState()

    DashboardContent(
        userName = currentUser?.name,
        nextClass = nextClass,
        nextAssignment = nextAssignment,
        pendingCount = pendingCount,
        onNavigateToTimetable = onNavigateToTimetable,
        onNavigateToAssignments = onNavigateToAssignments
    )
}

@Composable
fun DashboardContent(
    userName: String?,
    nextClass: TimetableEntry?,
    nextAssignment: Assignment?,
    pendingCount: Int,
    onNavigateToTimetable: () -> Unit,
    onNavigateToAssignments: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Welcome, ${userName ?: "Student"}!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                SummaryCard(
                    title = if (nextClass != null) "NEXT CLASS: ${nextClass.courseName}" else "NO CLASSES TODAY",
                    subtitle = if (nextClass != null) "Venue: ${nextClass.venue} @ ${nextClass.startTime}" else "Enjoy your free time!",
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    onClick = onNavigateToTimetable
                )
            }
            item {
                SummaryCard(
                    title = if (pendingCount > 0) "$pendingCount PENDING ASSIGNMENTS" else "ALL CAUGHT UP!",
                    subtitle = if (nextAssignment != null) "Next Due: ${nextAssignment.title}" else "No upcoming deadlines",
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    onClick = onNavigateToAssignments
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryCard(
    title: String,
    subtitle: String,
    containerColor: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = subtitle, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    com.example.ndejjeconnect.ui.theme.NdejjeConnectTheme {
        DashboardContent(
            userName = "John Doe",
            nextClass = null,
            nextAssignment = null,
            pendingCount = 3,
            onNavigateToTimetable = {},
            onNavigateToAssignments = {}
        )
    }
}
