package com.example.ndejjeconnect.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.example.ndejjeconnect.R
import com.example.ndejjeconnect.data.Academia
import com.example.ndejjeconnect.viewmodel.AuthViewModel
import com.example.ndejjeconnect.viewmodel.AuthState

/**
 * Register Screen: The "Sign Up" Desk.
 * 
 * Think of this screen like a student enrollment office. 
 * Before you can enter the main building (the Dashboard), you need to give the 
 * university your details (Name, Reg Number, Faculty) and create a password.
 * 
 * Once you "sign the book" (click Register), you are sent back to the 
 * "Check-in Desk" (Login Screen) to verify your new credentials.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var regNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedLevel by remember { mutableStateOf(Academia.levels[2]) } // Bachelor's Degree
    var selectedFaculty by remember { mutableStateOf(Academia.faculties[0]) } // Science & Computing
    var selectedCourse by remember { mutableStateOf("Bachelor of Computer Science (BCS)") }
    
    val levels = Academia.levels
    val faculties = Academia.faculties

    var levelExpanded by remember { mutableStateOf(false) }
    var facultyExpanded by remember { mutableStateOf(false) }
    var courseExpanded by remember { mutableStateOf(false) }

    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.RegisterSuccess) {
            onNavigateToLogin()
            viewModel.resetState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_large)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // --- STEP 1: Branding ---
        // Showing the University Name so users know where they are.
        Text(
            text = stringResource(id = R.string.university_name),
            fontSize = dimensionResource(id = R.dimen.text_size_large).value.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(id = R.string.create_account),
            fontSize = dimensionResource(id = R.dimen.text_size_medium).value.sp,
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_medium))
        )

        // --- STEP 2: Error Handling ---
        // If the "Brain" (ViewModel) says something went wrong, we show it here.
        if (authState is AuthState.Error) {
            Text(
                text = (authState as AuthState.Error).message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_medium))
            )
        }

        // --- STEP 3: Identity Details ---
        // Collecting the student's name and registration number.
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(stringResource(id = R.string.full_name)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = regNumber,
            onValueChange = { regNumber = it },
            label = { Text(stringResource(id = R.string.reg_number)) },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(8.dp))

        // --- STEP 4: Academic Selection ---
        // Dropdown menus to pick Level (e.g., Bachelor), Faculty, and Course.
        // It's like picking your department in the university.
        
        // Level Selection
        ExposedDropdownMenuBox(
            expanded = levelExpanded,
            onExpandedChange = { levelExpanded = !levelExpanded }
        ) {
            OutlinedTextField(
                value = selectedLevel,
                onValueChange = {},
                readOnly = true,
                label = { Text("Academic Level") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = levelExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = levelExpanded,
                onDismissRequest = { levelExpanded = false }
            ) {
                levels.forEach { level ->
                    DropdownMenuItem(
                        text = { Text(level) },
                        onClick = {
                            selectedLevel = level
                            levelExpanded = false
                            // Reset course selection when level changes
                            selectedCourse = Academia.getCourses(selectedFaculty, selectedLevel).firstOrNull() ?: "N/A"
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Faculty Selection
        ExposedDropdownMenuBox(
            expanded = facultyExpanded,
            onExpandedChange = { facultyExpanded = !facultyExpanded }
        ) {
            OutlinedTextField(
                value = selectedFaculty,
                onValueChange = {},
                readOnly = true,
                label = { Text("Faculty") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = facultyExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = facultyExpanded,
                onDismissRequest = { facultyExpanded = false }
            ) {
                faculties.forEach { faculty ->
                    DropdownMenuItem(
                        text = { Text(faculty) },
                        onClick = {
                            selectedFaculty = faculty
                            facultyExpanded = false
                            // Reset course selection when faculty changes
                            selectedCourse = Academia.getCourses(selectedFaculty, selectedLevel).firstOrNull() ?: "N/A"
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Course Selection
        val availableCourses = Academia.getCourses(selectedFaculty, selectedLevel)
        ExposedDropdownMenuBox(
            expanded = courseExpanded,
            onExpandedChange = { if (availableCourses.isNotEmpty()) courseExpanded = !courseExpanded }
        ) {
            OutlinedTextField(
                value = selectedCourse,
                onValueChange = {},
                readOnly = true,
                label = { Text("Program/Course") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = courseExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                placeholder = { if (availableCourses.isEmpty()) Text("No courses available for this level") }
            )
            if (availableCourses.isNotEmpty()) {
                ExposedDropdownMenu(
                    expanded = courseExpanded,
                    onDismissRequest = { courseExpanded = false }
                ) {
                    availableCourses.forEach { course ->
                        DropdownMenuItem(
                            text = { Text(course) },
                            onClick = {
                                selectedCourse = course
                                courseExpanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- STEP 5: Security ---
        // Setting up a password to protect the account.
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(id = R.string.password)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- STEP 6: Submission ---
        // Clicking this sends the details to the "Manager" (Repository) to save them.
        Button(
            onClick = { viewModel.register(name, regNumber, password, selectedLevel, selectedCourse) },
            modifier = Modifier.fillMaxWidth(),
            enabled = name.isNotBlank() && regNumber.isNotBlank() && password.isNotBlank() && 
                    selectedCourse != "N/A" && selectedCourse.isNotBlank() && authState !is AuthState.Loading
        ) {
            if (authState is AuthState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(dimensionResource(id = R.dimen.progress_indicator_size)),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = dimensionResource(id = R.dimen.progress_indicator_stroke)
                )
            } else {
                Text(stringResource(id = R.string.register))
            }
        }

        // Link to go back to Login if the user already has an account.
        TextButton(onClick = onNavigateToLogin) {
            Text(stringResource(id = R.string.already_have_account_login))
        }
    }
}
