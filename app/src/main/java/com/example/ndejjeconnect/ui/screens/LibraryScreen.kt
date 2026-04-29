package com.example.ndejjeconnect.ui.screens

import androidx.compose.foundation.background
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
import com.example.ndejjeconnect.data.local.BookLoan
import com.example.ndejjeconnect.data.local.SpaceBooking
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
            LibraryTopBar(onBackClick = onNavigateBack)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                LibrarySearchBar(
                    query = searchQuery,
                    onQueryChange = { libraryViewModel.updateSearchQuery(it) }
                )
            }

            if (loans.isNotEmpty() || bookings.isNotEmpty()) {
                item {
                    LibraryStatusCard(loans = loans, bookings = bookings)
                }
            }

            item {
                StudySpacesSection(
                    spaces = spaces,
                    onBookSpace = { space ->
                        libraryViewModel.bookSpace(
                            space, 
                            System.currentTimeMillis(), 
                            System.currentTimeMillis() + 3600000
                        )
                    }
                )
            }

            item {
                BookCollectionSection(
                    isSearchActive = searchQuery.isNotEmpty(),
                    books = books,
                    onReserveBook = { libraryViewModel.reserveBook(it) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LibraryTopBar(onBackClick: () -> Unit) {
    TopAppBar(
        title = { Text("Library Services") },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        }
    )
}

@Composable
private fun LibrarySearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Search title, author or ISBN...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = { Icon(Icons.Default.FilterList, contentDescription = null) },
        shape = MaterialTheme.shapes.medium,
        singleLine = true
    )
}

@Composable
private fun LibraryStatusCard(loans: List<BookLoan>, bookings: List<SpaceBooking>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "My Library Status",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (loans.isNotEmpty()) {
                Text(
                    text = "• ${loans.size} active books on loan",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            if (bookings.isNotEmpty()) {
                Text(
                    text = "• ${bookings.size} confirmed space bookings",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun StudySpacesSection(spaces: List<StudySpace>, onBookSpace: (StudySpace) -> Unit) {
    Column {
        Text(
            text = "Study Spaces",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(end = 16.dp)
        ) {
            items(spaces, key = { it.id }) { space ->
                SpaceCard(space = space, onBook = { onBookSpace(space) })
            }
        }
    }
}

@Composable
private fun BookCollectionSection(
    isSearchActive: Boolean,
    books: List<Book>,
    onReserveBook: (Book) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = if (isSearchActive) "Search Results" else "Featured Collection",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        books.forEach { book ->
            BookItem(book = book, onReserve = { onReserveBook(book) })
        }
    }
}

@Composable
fun BookItem(book: Book, onReserve: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(60.dp, 80.dp),
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Book, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = book.author,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                StatusChip(isAvailable = book.isAvailable)
            }
            
            if (book.isAvailable) {
                Button(
                    onClick = onReserve,
                    shape = MaterialTheme.shapes.small,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text("Reserve", style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}

@Composable
private fun StatusChip(isAvailable: Boolean) {
    val color = if (isAvailable) Color(0xFF2E7D32) else MaterialTheme.colorScheme.error
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = MaterialTheme.shapes.extraSmall
    ) {
        Text(
            text = if (isAvailable) "Available" else "On Loan",
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SpaceCard(space: StudySpace, onBook: () -> Unit) {
    Card(
        modifier = Modifier.width(160.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = if (space.isOccupied) 
                MaterialTheme.colorScheme.surfaceVariant 
            else 
                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = space.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = space.type,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onBook,
                modifier = Modifier.fillMaxWidth(),
                enabled = !space.isOccupied,
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = if (space.isOccupied) "Full" else "Book",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}
