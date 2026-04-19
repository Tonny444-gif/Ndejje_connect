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

/**
 * View Layer: The Hub (Dashboard Screen)
 * Provides students with a high-level overview of their academic day.
 */
@Composable
fun DashboardScreen(
    onNavigateToTimetable: () -> Unit,
    onNavigateToAssignments: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Welcome, Student!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                SummaryCard(
                    title = "NEXT CLASS: CSC 101",
                    subtitle = "Venue: Lab 1 @ 8:00 AM",
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    onClick = onNavigateToTimetable
                )
            }
            item {
                SummaryCard(
                    title = "2 PENDING ASSIGNMENTS",
                    subtitle = "Next Due: April 25",
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
        DashboardScreen(onNavigateToTimetable = {}, onNavigateToAssignments = {})
    }
}
