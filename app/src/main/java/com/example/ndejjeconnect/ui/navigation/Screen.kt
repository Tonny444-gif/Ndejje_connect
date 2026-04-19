package com.example.ndejjeconnect.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Dashboard : Screen("dashboard")
    object Timetable : Screen("timetable")
    object Assignments : Screen("assignments")
    object Notes : Screen("notes")
    object Profile : Screen("profile")
}
