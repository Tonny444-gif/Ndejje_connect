package com.example.ndejjeconnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.ndejjeconnect.data.local.User
import com.example.ndejjeconnect.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(
    viewModel: AuthViewModel,
    onNavigateBack: () -> Unit
) {
    val user by viewModel.currentUser.collectAsState()
    
    val faculty = Academia.academicStructure.entries.find { facultyEntry ->
        facultyEntry.value[user?.level]?.containsKey(user?.course) == true
    }?.key

    val units = Academia.getUnits(faculty, user?.level, user?.course)
    
    val semesterResults = units.map { unit ->
        val score = (70..98).random()
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
            ResultsTopBar(onBackClick = onNavigateBack)
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                StudentInfoHeader(user = user)

                if (semesterResults.isEmpty()) {
                    EmptyResultsView()
                } else {
                    ResultsList(results = semesterResults)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ResultsTopBar(onBackClick: () -> Unit) {
    TopAppBar(
        title = { Text("Academic Results") },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Navigate Back")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        )
    )
}

@Composable
private fun StudentInfoHeader(user: User?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = user?.name ?: "Student Name",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Registration: ${user?.regNumber ?: "N/A"}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                shape = MaterialTheme.shapes.extraSmall
            ) {
                Text(
                    text = user?.course ?: "Course Not Specified",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun EmptyResultsView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No academic records found for this period.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
private fun ResultsList(results: List<ResultItem>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(results) { result ->
            ResultCard(result = result)
        }
        
        item {
            Spacer(modifier = Modifier.height(8.dp))
            GPASummarySection(results = results)
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ResultCard(result: ResultItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
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
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Percentage Score: ${result.score}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            GradeBadge(grade = result.grade)
        }
    }
}

@Composable
private fun GradeBadge(grade: String) {
    val color = when(grade) {
        "A" -> Color(0xFF2E7D32)
        "B+" -> Color(0xFF558B2F)
        "B" -> Color(0xFF827717)
        else -> MaterialTheme.colorScheme.outline
    }
    
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = MaterialTheme.shapes.small,
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.2f))
    ) {
        Text(
            text = grade,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.titleMedium,
            color = color,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
fun GPASummarySection(results: List<ResultItem>) {
    val average = results.map { it.score }.average()
    val cgpa = (average / 100 * 5.0)
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Semester CGPA",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "%.2f".format(cgpa),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Academic Standing: Good",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

data class ResultItem(
    val unitName: String,
    val score: Int,
    val grade: String
)
