package com.example.ndejjeconnect.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.ndejjeconnect.R
import com.example.ndejjeconnect.data.Academia
import com.example.ndejjeconnect.viewmodel.AuthState
import com.example.ndejjeconnect.viewmodel.AuthViewModel

/**
 * RegisterScreen provides the user interface for creating a new student account.
 * It features dynamic course selection based on the selected academic level and faculty.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var regNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedLevel by remember { mutableStateOf(Academia.levels[2]) }
    var selectedFaculty by remember { mutableStateOf(Academia.faculties[0]) }
    var selectedCourse by remember { mutableStateOf("Bachelor of Computer Science (BCS)") }

    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.RegisterSuccess) {
            onNavigateToLogin()
            viewModel.resetState()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_large))
                .verticalScroll(rememberScrollState())
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            RegisterHeader()

            Spacer(modifier = Modifier.height(24.dp))

            if (authState is AuthState.Error) {
                Text(
                    text = (authState as AuthState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            RegistrationForm(
                name = name,
                onNameChange = { name = it },
                email = email,
                onEmailChange = { email = it },
                regNumber = regNumber,
                onRegNumberChange = { regNumber = it },
                password = password,
                onPasswordChange = { password = it },
                selectedLevel = selectedLevel,
                onLevelChange = { 
                    selectedLevel = it
                    selectedCourse = Academia.getCourses(selectedFaculty, it).firstOrNull() ?: ""
                },
                selectedFaculty = selectedFaculty,
                onFacultyChange = { 
                    selectedFaculty = it
                    selectedCourse = Academia.getCourses(it, selectedLevel).firstOrNull() ?: ""
                },
                selectedCourse = selectedCourse,
                onCourseChange = { selectedCourse = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            RegisterActions(
                isLoading = authState is AuthState.Loading,
                isFormValid = name.isNotBlank() && email.isNotBlank() && regNumber.isNotBlank() && 
                        password.isNotBlank() && selectedCourse.isNotBlank(),
                onRegisterClick = { 
                    viewModel.register(name, email, regNumber, password, selectedLevel, selectedCourse) 
                },
                onLoginClick = onNavigateToLogin
            )
        }
    }
}

@Composable
private fun RegisterHeader() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(id = R.string.university_name),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = stringResource(id = R.string.create_account),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegistrationForm(
    name: String,
    onNameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    regNumber: String,
    onRegNumberChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    selectedLevel: String,
    onLevelChange: (String) -> Unit,
    selectedFaculty: String,
    onFacultyChange: (String) -> Unit,
    selectedCourse: String,
    onCourseChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text(stringResource(id = R.string.full_name)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email Address") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = regNumber,
            onValueChange = onRegNumberChange,
            label = { Text(stringResource(id = R.string.reg_number)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        AcademicDropdown(
            label = "Academic Level",
            options = Academia.levels,
            selectedOption = selectedLevel,
            onOptionSelected = onLevelChange
        )

        AcademicDropdown(
            label = "Faculty",
            options = Academia.faculties,
            selectedOption = selectedFaculty,
            onOptionSelected = onFacultyChange
        )

        val availableCourses = Academia.getCourses(selectedFaculty, selectedLevel)
        AcademicDropdown(
            label = "Program/Course",
            options = availableCourses,
            selectedOption = selectedCourse,
            onOptionSelected = onCourseChange,
            enabled = availableCourses.isNotEmpty()
        )

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text(stringResource(id = R.string.password)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AcademicDropdown(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded && enabled,
        onExpandedChange = { if (enabled) expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            enabled = enabled
        )
        if (options.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun RegisterActions(
    isLoading: Boolean,
    isFormValid: Boolean,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = onRegisterClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = isFormValid && !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text(stringResource(id = R.string.register))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onLoginClick) {
            Text(stringResource(id = R.string.already_have_account_login))
        }
    }
}
