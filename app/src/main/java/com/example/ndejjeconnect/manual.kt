package com.example.ndejjeconnect

/**
 * # NDEJJE CONNECT - SYSTEM MANUAL & ARCHITECTURAL OVERVIEW
 * 
 * ## 1. PROJECT OVERVIEW
 * NdejjeConnect is a comprehensive Android application designed to streamline the academic 
 * and campus experience for students and staff at Ndejje University. It serves as a 
 * digital companion that integrates academic management, financial tracking, and 
 * library services into a single, cohesive platform.
 * 
 * ---
 * 
 * ## 2. CORE FUNCTIONALITIES
 * 
 * ### A. Identity & Security (The Entry Gate)
 * - **Registration:** Students can create accounts by providing their full name, 
 *   university email, registration number, academic level, and specific course.
 * - **Authentication:** A secure login system verifies credentials against the local 
 *   database. It includes input normalization (lowercase/trimming) to prevent access issues.
 * - **Password Recovery:** A two-step verification process (Reg Number + Email) 
 *   allows users to reset their password directly within the app.
 * 
 * ### B. Academic Suite (The Student Toolkit)
 * - **Timetable:** View and manage daily lecture schedules.
 * - **Assignments (Tasks):** A dedicated tracker to add, edit, and monitor academic 
 *   deadlines and coursework.
 * - **Notes:** A built-in note-taking utility for capturing lecture points.
 * - **Results:** A portal for students to check their academic performance.
 * 
 * ### C. Financial Management
 * - **Finance Portal:** Provides a clear view of tuition fees, payments made, and 
 *   outstanding balances, linked directly to the student's registration number.
 * 
 * ### D. Library Ecosystem
 * - **Student View:** Browse book categories, check availability, and manage loans.
 * - **Librarian Portal:** A specialized administrative interface (hidden from standard 
 *   navigation) allowing staff to manage book inventory and ISBN records.
 * 
 * ---
 * 
 * ## 3. TECHNICAL ARCHITECTURE (How It Works)
 * 
 * The app follows the **MVVM (Model-View-ViewModel)** architectural pattern, ensuring 
 * a clean separation of concerns and a reactive user interface.
 * 
 * ### I. THE DATA LAYER (Model)
 * - **Room Database (`AppDatabase`):** The engine for persistent storage. It manages 
 *   multiple tables: `users`, `books`, `notes`, `assignments`, `timetable`, and `finance`.
 * - **Repository (`MainRepository`):** Acts as the 'Single Source of Truth'. Every 
 *   data request from the UI goes through this layer, which orchestrates communication 
 *   with the various DAOs (Data Access Objects).
 * 
 * ### II. THE LOGIC LAYER (ViewModel)
 * - **`AuthViewModel`:** Manages the user session, registration, and security logic.
 * - **`LibraryViewModel`, `TimetableViewModel`, etc.:** Handle specific feature 
 *   logic, transforming raw database data into UI-ready states using Kotlin `Flow`.
 * 
 * ### III. THE UI LAYER (View)
 * - **Jetpack Compose:** The entire UI is built using declarative UI components.
 * - **Scaffold & Navigation:** `MainActivity` uses a `Scaffold` to provide a 
 *   consistent layout. The `NavHost` manages transitions between over 10 unique screens.
 * - **Conditional UI:** The Navigation Bar dynamically hides on authentication and 
 *   specialized portal screens (like the Librarian page) to maximize focus.
 * 
 * ---
 * 
 * ## 4. KEY TECHNICAL COMPONENTS
 * 
 * | Component | Responsibility |
 * |-----------|----------------|
 * | `MainActivity.kt` | The entry point, host of the navigation controller and top-level UI. |
 * | `Screen.kt` | A sealed class defining all available routes in the application. |
 * | `Academia.kt` | A helper object containing university-specific levels and courses. |
 * | `ViewModelFactory.kt` | Handles Dependency Injection for all ViewModels. |
 * | `NdejjeNavGraph.kt` | Defines the actual navigation paths and screen composables. |
 * 
 * ---
 * 
 * ## 5. UTMOST PRECISION NOTES
 * - **Data Integrity:** The app uses `OnConflictStrategy.REPLACE` in its DAOs to 
 *   ensure that updates to assignments or notes are handled smoothly without errors.
 * - **State Management:** Uses `StateFlow` to provide real-time UI updates when 
 *   data changes (e.g., adding a book immediately updates the inventory list).
 * - **UX Design:** Implements `Material 3` standards for a modern look and feel, 
 *   with responsive layouts for various student input forms.
 */
object SystemManual {
    const val VERSION = "1.0.0"
    const val AUTHOR = "EXAM_PROJECT_group6"
    const val UNIVERSITY = "Ndejje University"
}
