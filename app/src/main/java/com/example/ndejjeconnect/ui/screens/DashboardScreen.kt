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
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.automirrored.filled.MenuBook
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
import com.example.ndejjeconnect.R
import com.example.ndejjeconnect.data.local.*
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
    onNavigateToFinance: () -> Unit,
    onNavigateToLibrary: () -> Unit
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val nextClass by dashboardViewModel.nextClass.collectAsState()
    val nextAssignment by dashboardViewModel.nextAssignment.collectAsState()
    val pendingCount by dashboardViewModel.pendingAssignmentsCount.collectAsState()
    val gpa by dashboardViewModel.userGpa.collectAsState()
    val financeRecord by dashboardViewModel.financeRecord.collectAsState()
    val feedItems by dashboardViewModel.feedItems.collectAsState()

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
        financeRecord = financeRecord,
        feedItems = feedItems,
        onNavigateToTimetable = onNavigateToTimetable,
        onNavigateToAssignments = onNavigateToAssignments,
        onNavigateToProfile = onNavigateToProfile,
        onNavigateToResults = onNavigateToResults,
        onNavigateToFinance = onNavigateToFinance,
        onNavigateToLibrary = onNavigateToLibrary,
        onAddHighlight = { content -> dashboardViewModel.addHighlight(content) },
        onRemoveFeedItem = { item -> dashboardViewModel.removeFeedItem(item) }
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
    financeRecord: FinanceRecord?,
    feedItems: List<FeedItem>,
    onNavigateToTimetable: () -> Unit,
    onNavigateToAssignments: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToResults: () -> Unit,
    onNavigateToFinance: () -> Unit,
    onNavigateToLibrary: () -> Unit,
    onAddHighlight: (String) -> Unit,
    onRemoveFeedItem: (FeedItem) -> Unit
) {
    var isAddHighlightDialogOpen by remember { mutableStateOf(false) }

    if (isAddHighlightDialogOpen) {
        AddHighlightDialog(
            onDismiss = { isAddHighlightDialogOpen = false },
            onConfirm = { highlight ->
                onAddHighlight(highlight)
                isAddHighlightDialogOpen = false
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            DashboardHeader(onProfileClick = onNavigateToProfile)
        }

        item {
            UserInfoSection(
                userName = userName,
                userLevel = userLevel,
                userCourse = userCourse,
                userGpa = userGpa,
                onNavigateToResults = onNavigateToResults
            )
        }

        item {
            OverviewCards(
                nextClass = nextClass,
                pendingCount = pendingCount,
                onNavigateToTimetable = onNavigateToTimetable,
                onNavigateToAssignments = onNavigateToAssignments
            )
        }

        item {
            QuickActionsSection(
                onNavigateToResults = onNavigateToResults,
                onNavigateToLibrary = onNavigateToLibrary,
                onNavigateToFinance = onNavigateToFinance
            )
        }

        item {
            FinancialStatusCard(
                financeRecord = financeRecord,
                onNavigateToFinance = onNavigateToFinance
            )
        }

        item {
            FeedHeader(onAddClick = { isAddHighlightDialogOpen = true })
        }

        items(feedItems) { item ->
            NewsCard(
                item = item,
                modifier = Modifier.fillMaxWidth(),
                onDelete = { onRemoveFeedItem(item) }
            )
        }
    }
}

@Composable
private fun DashboardHeader(onProfileClick: () -> Unit) {
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
                .size(45.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .clickable { onProfileClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, contentDescription = "Profile")
        }
    }
}

@Composable
private fun UserInfoSection(
    userName: String?,
    userLevel: String?,
    userCourse: String?,
    userGpa: Double,
    onNavigateToResults: () -> Unit
) {
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

@Composable
private fun OverviewCards(
    nextClass: TimetableEntry?,
    pendingCount: Int,
    onNavigateToTimetable: () -> Unit,
    onNavigateToAssignments: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        BentoCard(
            modifier = Modifier.weight(1.5f).height(160.dp),
            title = "Next Up",
            content = nextClass?.courseName ?: "No more classes",
            subtitle = if (nextClass != null) "${nextClass.startTime} • ${nextClass.venue}" else "Free time!",
            icon = Icons.AutoMirrored.Filled.MenuBook,
            gradient = Brush.verticalGradient(listOf(Color(0xFF1A237E), Color(0xFF3949AB))),
            onClick = onNavigateToTimetable
        )

        BentoCard(
            modifier = Modifier.weight(1f).height(160.dp),
            title = "Tasks",
            content = "$pendingCount",
            subtitle = "Pending",
            icon = Icons.AutoMirrored.Filled.Assignment,
            gradient = Brush.verticalGradient(listOf(Color(0xFF004D40), Color(0xFF00897B))),
            onClick = onNavigateToAssignments
        )
    }
}

@Composable
private fun QuickActionsSection(
    onNavigateToResults: () -> Unit,
    onNavigateToLibrary: () -> Unit,
    onNavigateToFinance: () -> Unit
) {
    Column {
        Text(text = "Quick Actions", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            QuickActionButton(Icons.Default.BarChart, "Results", onClick = onNavigateToResults)
            QuickActionButton(Icons.AutoMirrored.Filled.LibraryBooks, "Library", onClick = onNavigateToLibrary)
            QuickActionButton(Icons.Default.Payment, "Finance", onClick = onNavigateToFinance)
        }
    }
}

@Composable
private fun FinancialStatusCard(
    financeRecord: FinanceRecord?,
    onNavigateToFinance: () -> Unit
) {
    val total = financeRecord?.totalFees ?: 0.0
    val paid = financeRecord?.amountPaid ?: 0.0
    val progress = if (total > 0) (paid / total).toFloat() else 0f
    val balance = (total - paid).coerceAtLeast(0.0)

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
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Paid: ${(progress * 100).toInt()}%", style = MaterialTheme.typography.bodySmall)
                Text(text = "Bal: ${balance.toLong()} UGX", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
private fun FeedHeader(onAddClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Campus Feed", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        IconButton(onClick = onAddClick) {
            Icon(Icons.Default.Add, contentDescription = "Add Highlight")
        }
    }
}

@Composable
private fun AddHighlightDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var highlightText by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Highlight") },
        text = {
            OutlinedTextField(
                value = highlightText,
                onValueChange = { highlightText = it },
                label = { Text("What's happening?") },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(onClick = {
                if (highlightText.isNotBlank()) {
                    onConfirm(highlightText)
                }
            }) { Text("Post") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
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
fun NewsCard(item: FeedItem, modifier: Modifier = Modifier, onDelete: () -> Unit) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.content,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium
            )
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Close, contentDescription = "Clear", modifier = Modifier.size(16.dp))
            }
        }
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
            financeRecord = null,
            feedItems = emptyList(),
            onNavigateToTimetable = {},
            onNavigateToAssignments = {},
            onNavigateToProfile = {},
            onNavigateToResults = {},
            onNavigateToFinance = {},
            onNavigateToLibrary = {},
            onAddHighlight = {},
            onRemoveFeedItem = {}
        )
    }
}
