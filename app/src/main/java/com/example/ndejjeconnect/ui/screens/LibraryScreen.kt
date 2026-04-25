package com.example.ndejjeconnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ndejjeconnect.data.local.Book
import com.example.ndejjeconnect.data.local.StudySpace
import com.example.ndejjeconnect.viewmodel.AuthViewModel
import com.example.ndejjeconnect.viewmodel.LibraryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    authViewModel: AuthViewModel,
    libraryViewModel: LibraryViewModel,
    onNavigateBack: () -> Unit
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val books by libraryViewModel.filteredBooks.collectAsState()
    val searchQuery by libraryViewModel.searchQuery.collectAsState()
    val spaces by libraryViewModel.allSpaces.collectAsState()
    val loans by libraryViewModel.userLoans.collectAsState()
    val bookings by libraryViewModel.userBookings.collectAsState()

    LaunchedEffect(currentUser) {
        libraryViewModel.setUser(currentUser)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Library Hub") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 1. Smart Search Header
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { libraryViewModel.updateSearchQuery(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search books or authors...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = { Icon(Icons.Default.FilterList, contentDescription = null) },
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // 2. My Status Card
            if (loans.isNotEmpty() || bookings.isNotEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "My Library Status",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            if (loans.isNotEmpty()) {
                                Text("${loans.size} items currently on loan/reserved")
                            }
                            if (bookings.isNotEmpty()) {
                                Text("${bookings.size} space bookings upcoming")
                            }
                        }
                    }
                }
            }

            // 3. Service Grid (Simplified for list)
            item {
                Text("Study Spaces", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(spaces) { space ->
                        SpaceCard(space) {
                             libraryViewModel.bookSpace(space, System.currentTimeMillis(), System.currentTimeMillis() + 3600000)
                        }
                    }
                }
            }

            // 4. Discovery / Search Results
            item {
                Text(
                    if (searchQuery.isEmpty()) "Featured Collection" else "Search Results",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            items(books) { book ->
                BookItem(book) {
                    libraryViewModel.reserveBook(book)
                }
            }
        }
    }
}

@Composable
fun BookItem(book: Book, onReserve: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp, 80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Book, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(book.title, fontWeight = FontWeight.Bold, maxLines = 1)
                Text(book.author, style = MaterialTheme.typography.bodySmall)
                Text(
                    text = if (book.isAvailable) "Available" else "On Loan",
                    color = if (book.isAvailable) Color(0xFF2E7D32) else MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall
                )
                if (book.isAvailable) {
                    Text(
                        "Loc: Floor ${book.floor}, Aisle ${book.aisle}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }
            }
            if (book.isAvailable) {
                Button(onClick = onReserve, shape = RoundedCornerShape(8.dp)) {
                    Text("Reserve")
                }
            }
        }
    }
}

@Composable
fun SpaceCard(space: StudySpace, onBook: () -> Unit) {
    Card(
        modifier = Modifier.width(160.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(space.name, fontWeight = FontWeight.Bold)
            Text(space.type, style = MaterialTheme.typography.bodySmall)
            Text("Cap: ${space.capacity}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onBook,
                modifier = Modifier.fillMaxWidth(),
                enabled = !space.isOccupied,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(if (space.isOccupied) "Full" else "Book")
            }
        }
    }
}
