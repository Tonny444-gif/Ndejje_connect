package com.example.ndejjeconnect.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Defines the navigation structure of the app.
 * Each screen has a unique [route] used for navigation and an optional [icon].
 */
sealed class Screen(val route: String, val icon: ImageVector? = null) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Dashboard : Screen("dashboard", Icons.Default.Dashboard)
    object Timetable : Screen("timetable", Icons.Default.Schedule)
    object Assignments : Screen("assignments", Icons.Default.Assignment)
    object EditAssignment : Screen("edit_assignment/{assignmentId}") {
        fun createRoute(assignmentId: Int) = "edit_assignment/$assignmentId"
    }
    object Notes : Screen("notes", Icons.Default.Note)
    object Profile : Screen("profile", Icons.Default.Person)
    object Results : Screen("results", Icons.Default.BarChart)
    object Finance : Screen("finance", Icons.Default.Payment)
    object Library : Screen("library", Icons.Default.LibraryBooks)
}

