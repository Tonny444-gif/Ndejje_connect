package com.example.ndejjeconnect.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ndejjeconnect.R
import com.example.ndejjeconnect.viewmodel.AuthState
import com.example.ndejjeconnect.viewmodel.AuthViewModel

/**
 * LoginScreen serves as the entry point for user authentication.
 * It manages the UI state for login attempts and handles navigation upon success.
 */
@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToPassreset: () -> Unit,
    onNavigateToLibrarian: () -> Unit
) {
    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            onLoginSuccess()
            viewModel.resetState()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LoginContent(
            authState = authState,
            onLoginAction = { email, pass -> viewModel.login(email, pass) },
            onRegisterNavigation = onNavigateToRegister,
            onPassresetNavigation = onNavigateToPassreset,
            onLibrarianNavigation = onNavigateToLibrarian
        )
    }
}

@Composable
private fun LoginContent(
    authState: AuthState,
    onLoginAction: (String, String) -> Unit,
    onRegisterNavigation: () -> Unit,
    onPassresetNavigation: () -> Unit,
    onLibrarianNavigation: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_large))
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LoginHeader()

        Spacer(modifier = Modifier.height(32.dp))

        AuthenticationStatus(authState = authState)

        LoginForm(
            email = email,
            onEmailChange = { email = it },
            password = password,
            onPasswordChange = { password = it }
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = onPassresetNavigation,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Forgot Password?")
        }

        Spacer(modifier = Modifier.height(24.dp))

        LoginActions(
            isLoading = authState is AuthState.Loading,
            onLoginClick = { onLoginAction(email, password) },
            onRegisterClick = onRegisterNavigation,
            onLibrarianClick = onLibrarianNavigation
        )
    }
}

@Composable
private fun LoginHeader() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(id = R.string.student_portal),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
private fun AuthenticationStatus(authState: AuthState) {
    when (authState) {
        is AuthState.Error -> {
            Text(
                text = authState.message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        is AuthState.RegisterSuccess -> {
            Text(
                text = stringResource(id = R.string.registration_successful),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        else -> Unit
    }
}

@Composable
private fun LoginForm(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email Address") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = MaterialTheme.shapes.medium
        )

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text(stringResource(id = R.string.password)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = MaterialTheme.shapes.medium
        )
    }
}

@Composable
private fun LoginActions(
    isLoading: Boolean,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onLibrarianClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = MaterialTheme.shapes.medium,
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text(text = stringResource(id = R.string.login))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onRegisterClick) {
                Text(
                    text = stringResource(id = R.string.no_account_signup),
                    style = MaterialTheme.typography.labelLarge
                )
            }

            TextButton(onClick = onLibrarianClick) {
                Text(
                    text = "Librarian",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
