package com.example.ndejjeconnect.ui.navigation

/**
 * Defines the navigation structure of the app.
 * Each screen has a unique [route] used for navigation.
 */
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Dashboard : Screen("dashboard")
    object Timetable : Screen("timetable")
    object Assignments : Screen("assignments")
    object EditAssignment : Screen("edit_assignment/{assignmentId}") {
        fun createRoute(assignmentId: Int) = "edit_assignment/$assignmentId"
    }
    object Notes : Screen("notes")
    object Profile : Screen("profile")
    object Results : Screen("results")
    object Finance : Screen("finance")
    object Library : Screen("library")
}
