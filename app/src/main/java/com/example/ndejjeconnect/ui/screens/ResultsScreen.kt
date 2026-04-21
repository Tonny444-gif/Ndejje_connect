package com.example.ndejjeconnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ndejjeconnect.data.Academia
import com.example.ndejjeconnect.viewmodel.AuthViewModel

/**
 * ResultsScreen is the "Report Card" of the app.
 * It automatically looks up the units for a student's course and shows their grades.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(
    viewModel: AuthViewModel,
    onNavigateBack: () -> Unit
) {
    // 1. GETTING THE STUDENT'S INFO
    val user by viewModel.currentUser.collectAsState()
    
    // 2. FETCHING ACADEMIC DATA
    // The app looks through its "School Registry" (Academia.kt) to find which Faculty the student belongs to
    val faculty = Academia.academicStructure.entries.find { facultyEntry ->
        facultyEntry.value[user?.level]?.containsKey(user?.course) == true
    }?.key

    // Get the list of units (subjects) for this specific student
    val units = Academia.getUnits(faculty, user?.level, user?.course)
    
    // 3. GENERATING RESULTS
    // For this demo, we create some random scores for the student's units
    val demoResults = units.map { unit ->
        val score = (70..95).random()
        val grade = when {
            score >= 80 -> "A"
            score >= 75 -> "B+"
            score >= 70 -> "B"
            else -> "C"
        }
        ResultItem(unit, score, grade)
    }

    Scaffold(
        topBar = {
            // Screen Header
            TopAppBar(
                title = { Text("Academic Results") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F5F5)) // Light gray background
        ) {
            // STUDENT HEADER: Shows Name, Reg Number, and Course
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = user?.name ?: "Student",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Reg No: ${user?.regNumber ?: "N/A"}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = user?.course ?: "N/A",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // LIST OF GRADES
            if (demoResults.isEmpty()) {
                // If no units were found
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No results available for your course yet.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Show a card for each subject/unit
                    items(demoResults) { result ->
                        ResultCard(result)
                    }
                    
                    // Show the final GPA summary at the bottom
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        GPASection(demoResults)
                    }
                }
            }
        }
    }
}

/**
 * ResultCard displays a single subject's name, score, and letter grade.
 */
@Composable
fun ResultCard(result: ResultItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = result.unitName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Score: ${result.score}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            
            // Colored box for the Grade (Green for A, Light Green for B, etc.)
            Surface(
                color = when(result.grade) {
                    "A" -> Color(0xFF4CAF50)
                    "B+" -> Color(0xFF8BC34A)
                    else -> Color(0xFFCDDC39)
                }.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = result.grade,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    color = when(result.grade) {
                        "A" -> Color(0xFF2E7D32)
                        "B+" -> Color(0xFF558B2F)
                        else -> Color(0xFF827717)
                    },
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}

/**
 * GPASection calculates and displays the final grade average for the semester.
 */
@Composable
fun GPASection(results: List<ResultItem>) {
    // Simple math to get the average
    val average = results.map { it.score }.average()
    val cgpa = (average / 100 * 5.0) // Convert percentage to a 5.0 scale
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Current Semester CGPA", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "%.2f".format(cgpa),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
                text = "Academic Standing: Good",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
            )
        }
    }
}

/**
 * ResultItem is a small data holder for a single subject's score info.
 */
data class ResultItem(
    val unitName: String,
    val score: Int,
    val grade: String
)
