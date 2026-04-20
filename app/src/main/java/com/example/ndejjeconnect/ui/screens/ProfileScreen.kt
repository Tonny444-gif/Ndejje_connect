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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
 * View Layer: Profile Screen
 * Displays student information and app settings.
 * MVVM Pattern: Observes AuthViewModel for user data.
 */
@Composable
fun ProfileScreen(
    viewModel: AuthViewModel,
    onLogout: () -> Unit,
    onNavigateToEditProfile: () -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Section 1: Profile Header (Image and Basic Info)
        ProfileHeader(
            name = currentUser?.name ?: "Unknown Student",
            regNumber = currentUser?.regNumber ?: "N/A"
        )

        Spacer(modifier = Modifier.height(32.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        // Section 2: Action List (Settings, Edit, Logout)
        ProfileMenuItem(
            icon = Icons.Default.Brightness4,
            label = "Settings: Dark/Light Mode",
            onClick = { /* Toggle Theme Logic */ }
        )

        ProfileMenuItem(
            icon = Icons.Default.Edit,
            label = "Edit Profile Details",
            onClick = onNavigateToEditProfile
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
