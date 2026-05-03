# NdejjeConnect

NdejjeConnect is a modern Android application designed as a comprehensive student portal for Ndejje University. Built with Jetpack Compose and following Clean Architecture principles, it integrates academic management, library services, and student life into a single, intuitive interface.

Features

### Academic Management
*   **Dynamic Registration**: Interactive enrollment process with real-time course filtering based on faculties and academic levels (Certificates to PhDs).
*   **Timetable**: Manage and view your weekly lecture schedule with location tracking and time management.
*   **Assignments & Tasks**: Track coursework deadlines with priority levels and completion status.
*   **Notes**: Digital notebook for organizing course unit materials and attachments.
*   **Academic Results**: Real-time view of semester grades and CGPA calculation based on the official university structure.

### Library Services
*   **Digital Catalog**: Search through the university's book inventory by title or author.
*   **Reservations**: Digitally reserve books for later pickup.
*   **Study Spaces**: Book library pods, desks, or group rooms for focused sessions.
*   **Librarian Portal**: Administrative interface for managing the library's physical and digital inventory.

###  Finance & Campus Life
*   **Finance Tracker**: Monitor tuition fees, payments made, and outstanding balances.
*   **Campus Feed**: Stay updated with the latest news, highlights, and announcements from the university community.

## Tech Stack

*   **Language**: [Kotlin](https://kotlinlang.org/)
*   **UI Framework**: [Jetpack Compose](https://developer.android.com/compose) (Material 3)
*   **Architecture**: MVVM (Model-View-ViewModel) with Clean Architecture patterns
*   **Database**: [Room](https://developer.android.com/training/data-storage/room) for offline-first persistence
*   **Navigation**: Jetpack Compose Navigation
*   **Concurrency**: Kotlin Coroutines & Flow
*   **Dependency Injection**: ViewModel Factory pattern
*   **Annotation Processing**: KSP (Kotlin Symbol Processing)

## Project Structure

```text
com.example.ndejjeconnect
├── data
│   ├── local          # Room Database, DAOs, and Entities
│   ├── Academia       # Static Ndejje University academic data
│   └── Repository     # MainRepository (Single source of truth)
├── ui
│   ├── navigation     # Navigation routes and Screen definitions
│   ├── screens        # Individual Compose screens (Login, Dashboard, etc.)
│   └── theme          # Custom Material3 styling and colors
├── viewmodel          # ViewModels for business logic and state management
│   └── factory        # ViewModelProvider Factory for DI
└── MainActivity.kt    # Entry point and NavHost configuration
```

##  Setup & Installation

1.  **Clone the repository**:
    ```bash
    git clone https://github.com/your-repo/ndejjeconnect.git
    ```
2.  **Open in Android Studio**:
    *   Recommended: Android Studio Ladybug or newer.
3.  **Sync Gradle**:
    *   Ensure all dependencies are downloaded.
4.  **Run the application**:
    *   Supports Android API 24 (Nougat) and above.

## License

This project is developed for academic purposes as part of the Ndejje University Group 6 Examination Project.


https://youtu.be/pIq9j98JPe4?si=k91dHaMvzQ3uKiNr
