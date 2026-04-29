package com.example.ndejjeconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ndejjeconnect.data.MainRepository
import com.example.ndejjeconnect.data.local.AppDatabase
import com.example.ndejjeconnect.ui.navigation.Screen
import com.example.ndejjeconnect.ui.screens.*
import com.example.ndejjeconnect.ui.theme.NdejjeConnectTheme
import com.example.ndejjeconnect.viewmodel.*
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
        // Using lazy or a background approach would be better, but for now we ensure 
        // it doesn't block the initial composition significantly.
        val database by lazy { AppDatabase.getDatabase(this) }
        val repository by lazy { 
            MainRepository(
                database.noteDao(),
                database.assignmentDao(),
                database.timetableDao(),
                database.userDao(),
                database.financeDao(),
                database.feedDao(),
                database.libraryDao()
            )
        }
        // ViewModel Factory for dependency injection
        val factory by lazy { ViewModelFactory(repository) }

        setContent {
            NdejjeConnectTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // Shared AuthViewModel for session management
                val authViewModel: AuthViewModel = viewModel(factory = factory)

                Scaffold(
                    bottomBar = {
                        // View Logic: Only show bottom navigation if NOT on Login or Register screens
                        if (currentRoute != Screen.Login.route && currentRoute != Screen.Register.route && currentRoute != null) {
                            NavigationBar {
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                                    label = { Text("Home") },
                                    selected = currentRoute == Screen.Dashboard.route,
                                    onClick = { 
                                        navController.navigate(Screen.Dashboard.route) {
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
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
                                    icon = { Icon(Icons.AutoMirrored.Filled.Assignment, contentDescription = "Assignments") },
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
                                    icon = { Icon(Icons.AutoMirrored.Filled.Notes, contentDescription = "Notes") },
                                    label = { Text("Notes") },
                                    selected = currentRoute == Screen.Notes.route,
                                    onClick = { 
                                        navController.navigate(Screen.Notes.route) {
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                                    label = { Text("Profile") },
                                    selected = currentRoute == Screen.Profile.route,
                                    onClick = {
                                        navController.navigate(Screen.Profile.route) {
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
                            LoginScreen(
                                viewModel = authViewModel,
                                onLoginSuccess = {
                                    navController.navigate(Screen.Dashboard.route) {
                                        popUpTo(Screen.Login.route) { inclusive = true }
                                    }
                                },
                                onNavigateToRegister = {
                                    navController.navigate(Screen.Register.route)
                                },
                                onNavigateToPassreset = {
                                    navController.navigate(Screen.Passreset.route)
                                },
                                onNavigateToLibrarian = {
                                    navController.navigate(Screen.Librarian.route)
                                }
                            )
                        }
                        composable(Screen.Passreset.route) {
                            PassresetScreen(
                                viewModel = authViewModel,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        composable(Screen.Librarian.route) {
                            val libraryViewModel: LibraryViewModel = viewModel(factory = factory)
                            LibrarianScreen(
                                viewModel = libraryViewModel,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        composable(Screen.Register.route) {
                            RegisterScreen(
                                viewModel = authViewModel,
                                onRegisterSuccess = {
                                    // This callback is triggered when registration is successful.
                                    // The logic in RegisterScreen now navigates to log in.
                                },
                                onNavigateToLogin = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        composable(Screen.Dashboard.route) {
                            val dashboardViewModel: DashboardViewModel = viewModel(factory = factory)
                            DashboardScreen(
                                authViewModel = authViewModel,
                                dashboardViewModel = dashboardViewModel,
                                onNavigateToTimetable = { navController.navigate(Screen.Timetable.route) },
                                onNavigateToAssignments = { navController.navigate(Screen.Assignments.route) },
                                onNavigateToProfile = { navController.navigate(Screen.Profile.route) },
                                onNavigateToResults = { navController.navigate(Screen.Results.route) },
                                onNavigateToFinance = { navController.navigate(Screen.Finance.route) },
                                onNavigateToLibrary = { navController.navigate(Screen.Library.route) }
                            )
                        }
                        composable(Screen.Results.route) {
                            ResultsScreen(
                                viewModel = authViewModel,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        composable(Screen.Finance.route) {
                            val dashboardViewModel: DashboardViewModel = viewModel(factory = factory)
                            FinanceScreen(
                                authViewModel = authViewModel,
                                dashboardViewModel = dashboardViewModel,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        composable(Screen.Library.route) {
                            val libraryViewModel: LibraryViewModel = viewModel(factory = factory)
                            LibraryScreen(
                                authViewModel = authViewModel,
                                libraryViewModel = libraryViewModel,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        composable(Screen.Timetable.route) {
                            val timetableViewModel: TimetableViewModel = viewModel(factory = factory)
                              TimetableScreen(viewModel = timetableViewModel, authViewModel = authViewModel)
                        }
                        composable(Screen.Assignments.route) {
                            val assignmentsViewModel: AssignmentsViewModel = viewModel(factory = factory)
                            AssignmentsScreen(
                                viewModel = assignmentsViewModel,
                                onEditAssignment = { id ->
                                    navController.navigate(Screen.EditAssignment.createRoute(id))
                                }
                            )
                        }
                        composable(
                            route = Screen.EditAssignment.route,
                            arguments = listOf(navArgument("assignmentId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getInt("assignmentId") ?: 0
                            val assignmentsViewModel: AssignmentsViewModel = viewModel(factory = factory)
                            EditAssignmentScreen(
                                assignmentId = id,
                                viewModel = assignmentsViewModel,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        composable(Screen.Notes.route) {
                            val notesViewModel: NotesViewModel = viewModel(factory = factory)
                            NotesScreen(viewModel = notesViewModel)
                        }
                        composable(Screen.Profile.route) {
                            ProfileScreen(
                                viewModel = authViewModel,
                                onLogout = {
                                    navController.navigate(Screen.Login.route) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                },
                                onNavigateToEditProfile = { /* TODO */ }
                            )
                        }
                    }
                }
            }
        }
    }
}
