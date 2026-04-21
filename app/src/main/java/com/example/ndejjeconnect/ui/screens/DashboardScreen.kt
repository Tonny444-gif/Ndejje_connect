package com.example.ndejjeconnect.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ndejjeconnect.R
import com.example.ndejjeconnect.data.local.Assignment
import com.example.ndejjeconnect.data.local.TimetableEntry
import com.example.ndejjeconnect.viewmodel.AuthViewModel
import com.example.ndejjeconnect.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    authViewModel: AuthViewModel,
    dashboardViewModel: DashboardViewModel,
    onNavigateToTimetable: () -> Unit,
    onNavigateToAssignments: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToResults: () -> Unit,
    onNavigateToFinance: () -> Unit
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val nextClass by dashboardViewModel.nextClass.collectAsState()
    val nextAssignment by dashboardViewModel.nextAssignment.collectAsState()
    val pendingCount by dashboardViewModel.pendingAssignmentsCount.collectAsState()
    val gpa by dashboardViewModel.userGpa.collectAsState()

    LaunchedEffect(currentUser) {
        dashboardViewModel.setUser(currentUser)
    }

    DashboardContent(
        userName = currentUser?.name,
        userLevel = currentUser?.level,
        userCourse = currentUser?.course,
        nextClass = nextClass,
        nextAssignment = nextAssignment,
        pendingCount = pendingCount,
        userGpa = gpa,
        onNavigateToTimetable = onNavigateToTimetable,
        onNavigateToAssignments = onNavigateToAssignments,
        onNavigateToProfile = onNavigateToProfile,
        onNavigateToResults = onNavigateToResults,
        onNavigateToFinance = onNavigateToFinance
    )
}

@Composable
fun DashboardContent(
    userName: String?,
    userLevel: String?,
    userCourse: String?,
    nextClass: TimetableEntry?,
    nextAssignment: Assignment?,
    pendingCount: Int,
    userGpa: Double,
    onNavigateToTimetable: () -> Unit,
    onNavigateToAssignments: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToResults: () -> Unit,
    onNavigateToFinance: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // 1. Top Branding & Profile Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(100.dp),
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ndejje_logo),
                        contentDescription = "University Logo",
                        modifier = Modifier.padding(16.dp),
                        contentScale = ContentScale.Fit
                    )
                }
                
                Box(
                    modifier = Modifier
                        .size(45.dp) // Slightly larger avatar
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .clickable { onNavigateToProfile() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = "Profile")
                }
            }
        }

        item {
            Column {
                Text(
                    text = "Hello, ${userName?.split(" ")?.firstOrNull() ?: "Student"}!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "$userLevel in $userCourse",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SuggestionChip(
                        onClick = onNavigateToResults, 
                        label = { Text("GPA: ${"%.2f".format(userGpa)}") }
                    )
                }
            }
        }

        // 2. Bento Box Style Widgets
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                // Next Up Card (Large)
                BentoCard(
                    modifier = Modifier.weight(1.5f).height(160.dp),
                    title = "Next Up",
                    content = if (nextClass != null) nextClass.courseName else "No more classes",
                    subtitle = if (nextClass != null) "${nextClass.startTime} • ${nextClass.venue}" else "Free time!",
                    icon = Icons.Default.MenuBook,
                    gradient = Brush.verticalGradient(listOf(Color(0xFF1A237E), Color(0xFF3949AB))),
                    onClick = onNavigateToTimetable
                )
                
                // Tasks Card (Small)
                BentoCard(
                    modifier = Modifier.weight(1f).height(160.dp),
                    title = "Tasks",
                    content = "$pendingCount",
                    subtitle = "Pending",
                    icon = Icons.Default.Assignment,
                    gradient = Brush.verticalGradient(listOf(Color(0xFF004D40), Color(0xFF00897B))),
                    onClick = onNavigateToAssignments
                )
            }
        }

        // 3. Quick Actions Row
        item {
            Text(text = "Quick Actions", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                QuickActionButton(Icons.Default.BarChart, "Results", onClick = onNavigateToResults)
                QuickActionButton(Icons.Default.LibraryBooks, "Library", onClick = {})
            }
        }

        // 4. Financial Status Widget
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToFinance() }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Financial Status", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { 0.75f },
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Tuition Paid: 75%", style = MaterialTheme.typography.bodySmall)
                        Text(text = "Balance: 1.2M UGX", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }

        // 5. Campus Feed (Vertical)
        item {
            Text(text = "Campus Feed", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        val newsItems = listOf(
            "Library closing at 5 PM today",
            "End of Semester Exams start June 10th",
            "Guild elections tomorrow at Main Campus",
            "Inter-Faculty Sports Gala: Registration Open",
            "New E-Resources added to Digital Library",
            "Career Fair: Meet top employers on Friday",
            "Main Hall renovation completed",
            "Scholarship applications for next intake open",
            "Health Awareness Week starting Monday",
            "Coding Bootcamp: Learn Kotlin & Compose",
            "Art Exhibition: Creative Arts Department",
            "Guest Lecture: Tech Innovation in Africa",
            "Lost & Found: Keys found near Cafeteria"
        )

        items(newsItems) { news ->
            NewsCard(news, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun BentoCard(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    subtitle: String,
    icon: ImageVector,
    gradient: Brush,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
    ) {
        Box(modifier = Modifier.fillMaxSize().background(gradient).padding(16.dp)) {
            Icon(
                icon, 
                contentDescription = null, 
                modifier = Modifier.align(Alignment.TopEnd).size(32.dp).alpha(0.3f),
                tint = Color.White
            )
            Column(modifier = Modifier.align(Alignment.BottomStart)) {
                Text(text = title, color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.labelMedium)
                Text(text = content, color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, maxLines = 2)
                Text(text = subtitle, color = Color.White.copy(alpha = 0.9f), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun QuickActionButton(icon: ImageVector, label: String, onClick: () -> Unit = {}) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Surface(
            modifier = Modifier.size(56.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.secondaryContainer
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = label, tint = MaterialTheme.colorScheme.onSecondaryContainer)
            }
        }
        Text(text = label, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(top = 4.dp))
    }
}

@Composable
fun NewsCard(text: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    com.example.ndejjeconnect.ui.theme.NdejjeConnectTheme {
        DashboardContent(
            userName = "Ronald",
            userLevel = "Degree",
            userCourse = "Computer Science",
            nextClass = null,
            nextAssignment = null,
            pendingCount = 3,
            userGpa = 3.8,
            onNavigateToTimetable = {},
            onNavigateToAssignments = {},
            onNavigateToProfile = {},
            onNavigateToResults = {},
            onNavigateToFinance = {}
        )
    }
}
