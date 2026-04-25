package com.example.ndejjeconnect.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ndejjeconnect.viewmodel.AuthViewModel

/**
 * Profile Screen: The "Student ID Card".
 * 
 * Think of this screen like your personal student portal. 
 * It shows your "Photo" (avatar) and "ID Details" (Name and Reg Number).
 * 
 * - The "Brain" (AuthViewModel) knows who is currently logged in.
 * - You can "Update your ID" (Edit Profile) or "Exit the Building" (Logout).
 */
@Composable
fun ProfileScreen(
    viewModel: AuthViewModel,
    onLogout: () -> Unit,
    onNavigateToEditProfile: () -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsState()
    var showEditDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- SECTION 1: The ID Card ---
        // Showing your face and your name.
        ProfileHeader(
            name = currentUser?.name ?: "Unknown Student",
            regNumber = currentUser?.regNumber ?: "N/A"
        )

        Spacer(modifier = Modifier.height(32.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        // --- SECTION 2: Things you can do ---
        // Change your name or sign out.
        ProfileMenuItem(
            icon = Icons.Default.Edit,
            label = "Edit Profile Details",
            onClick = { showEditDialog = true }
        )

        ProfileMenuItem(
            icon = Icons.Default.Logout,
            label = "Logout",
            labelColor = MaterialTheme.colorScheme.error,
            onClick = {
                viewModel.logout()
                onLogout()
            }
        )
    }

    if (showEditDialog && currentUser != null) {
        // --- SECTION 3: Editing ---
        // A small popup to change your name.
        EditProfileDialog(
            currentName = currentUser?.name ?: "",
            onDismiss = { showEditDialog = false },
            onSave = { newName ->
                viewModel.updateProfile(newName, currentUser?.profileImageUri)
                showEditDialog = false
            }
        )
    }
}

/**
 * Edit Dialog: The "Correction Pen".
 * Use this to fix a typo in your name.
 */
@Composable
fun EditProfileDialog(
    currentName: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var name by remember { mutableStateOf(currentName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Profile") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSave(name) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

/**
 * Renders the top part of the profile with the user's avatar and ID.
 */
@Composable
fun ProfileHeader(name: String, regNumber: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Profile Picture Placeholder
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile Picture",
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = name,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Reg: $regNumber",
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

/**
 * A reusable row for profile menu options.
 */
@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    label: String,
    labelColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = labelColor)
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = label,
                fontSize = 16.sp,
                color = labelColor,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
