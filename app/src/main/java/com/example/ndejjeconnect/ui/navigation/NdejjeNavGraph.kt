package com.example.ndejjeconnect.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ndejjeconnect.ui.screens.*
import com.example.ndejjeconnect.viewmodel.*
import com.example.ndejjeconnect.viewmodel.factory.ViewModelFactory

@Composable
fun NdejjeNavGraph(
    navController: NavHostController,
    factory: ViewModelFactory,
    authViewModel: AuthViewModel,
    innerPadding: PaddingValues
) {
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
                }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = {
                    // This callback is triggered when registration is successful.
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
