package com.example.ndejjeconnect.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ndejjeconnect.viewmodel.AuthViewModel

/**
 * Profile Screen: Displays user information and provides account management options.
 */
@Composable
fun ProfileScreen(
    viewModel: AuthViewModel,
    onLogout: () -> Unit,
    onNavigateToEditProfile: () -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsState()
    var isEditDialogVisible by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Information Section
            ProfileHeader(
                name = currentUser?.name ?: "Unknown Student",
                regNumber = currentUser?.regNumber ?: "N/A"
            )

            Spacer(modifier = Modifier.height(40.dp))
            HorizontalDivider(modifier = Modifier.alpha(0.5f))
            Spacer(modifier = Modifier.height(24.dp))

            // Account Actions Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProfileMenuItem(
                    icon = Icons.Default.Edit,
                    label = "Edit Profile Details",
                    onClick = { isEditDialogVisible = true }
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
        }
    }

    // Dialogs
    if (isEditDialogVisible && currentUser != null) {
        EditProfileDialog(
            currentName = currentUser?.name ?: "",
            onDismiss = { isEditDialogVisible = false },
            onSave = { newName ->
                viewModel.updateProfile(newName, currentUser?.profileImageUri)
                isEditDialogVisible = false
            }
        )
    }
}

/**
 * Header component displaying the user's avatar and primary identification.
 */
@Composable
fun ProfileHeader(name: String, regNumber: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Avatar Box
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile Avatar",
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Registration: $regNumber",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Reusable menu item for profile actions.
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
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = labelColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = labelColor,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/**
 * Dialog for updating user profile information.
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
        title = { Text("Update Profile") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(onClick = { if (name.isNotBlank()) onSave(name) }) {
                Text("Save Changes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
