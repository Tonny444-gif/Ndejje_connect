package com.example.ndejjeconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ndejjeconnect.data.MainRepository
import com.example.ndejjeconnect.data.local.AppDatabase
import com.example.ndejjeconnect.ui.navigation.Screen
import com.example.ndejjeconnect.ui.screens.*
import com.example.ndejjeconnect.ui.theme.NdejjeConnectTheme
import com.example.ndejjeconnect.viewmodel.NotesViewModel
import com.example.ndejjeconnect.viewmodel.factory.ViewModelFactory

/**
 * Main Activity: The Entry Point of the Application.
 * Integrates MVVM architecture, Room Database, and Jetpack Compose Navigation.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Model Initialization: Room Database and Main Repository
        val database = AppDatabase.getDatabase(this)
        val repository = MainRepository(
            database.noteDao(),
            database.assignmentDao(),
            database.timetableDao()
        )
        // ViewModel Factory for dependency injection
        val factory = ViewModelFactory(repository)

        setContent {
            NdejjeConnectTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Scaffold(
                    bottomBar = {
                        // View Logic: Only show bottom navigation if not on the login screen
                        if (currentRoute != Screen.Login.route && currentRoute != null) {
                            NavigationBar {
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                                    label = { Text("Home") },
                                    selected = currentRoute == Screen.Dashboard.route,
                                    onClick = { 
                                        navController.navigate(Screen.Dashboard.route) {
                                            popUpTo(Screen.Dashboard.route) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.CalendarMonth, contentDescription = "Timetable") },
                                    label = { Text("Timetable") },
                                    selected = currentRoute == Screen.Timetable.route,
                                    onClick = { 
                                        navController.navigate(Screen.Timetable.route) {
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.Assignment, contentDescription = "Assignments") },
                                    label = { Text("Tasks") },
                                    selected = currentRoute == Screen.Assignments.route,
                                    onClick = { 
                                        navController.navigate(Screen.Assignments.route) {
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.Notes, contentDescription = "Notes") },
                                    label = { Text("Notes") },
                                    selected = currentRoute == Screen.Notes.route,
                                    onClick = { 
                                        navController.navigate(Screen.Notes.route) {
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    // UI Navigation: Defining the screen transitions
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Login.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Login.route) {
                            LoginScreen(onLoginSuccess = {
                                navController.navigate(Screen.Dashboard.route) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                            })
                        }
                        composable(Screen.Dashboard.route) {
                            DashboardScreen(
                                onNavigateToTimetable = { navController.navigate(Screen.Timetable.route) },
                                onNavigateToAssignments = { navController.navigate(Screen.Assignments.route) }
                            )
                        }
                        composable(Screen.Timetable.route) {
                            TimetableScreen()
                        }
                        composable(Screen.Assignments.route) {
                            AssignmentsScreen()
                        }
                        composable(Screen.Notes.route) {
                            // ViewModel Layer: Injecting the scoped ViewModel into the screen
                            val notesViewModel: NotesViewModel = viewModel(factory = factory)
                            NotesScreen(viewModel = notesViewModel)
                        }
                    }
                }
            }
        }
    }
}
