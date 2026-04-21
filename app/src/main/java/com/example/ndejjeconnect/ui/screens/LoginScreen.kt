package com.example.ndejjeconnect.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.example.ndejjeconnect.R
import com.example.ndejjeconnect.viewmodel.AuthViewModel
import com.example.ndejjeconnect.viewmodel.AuthState

/**
 * LoginScreen is the "Check-in Desk" of the app.
 * It manages the user's login status and decides when to move to the Dashboard.
 */
@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    // Watches the login status (Idle, Loading, Success, or Error)
    val authState by viewModel.authState.collectAsState()

    // When the status changes to "Success", move the user to the next screen
    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            onLoginSuccess()
            viewModel.resetState() // Clear the state so it's fresh for next time
        }
    }

    // Show the actual UI elements (the form)
    LoginContent(
        authState = authState,
        onLogin = { reg, pass -> viewModel.login(reg, pass) },
        onNavigateToRegister = onNavigateToRegister
    )
}

/**
 * LoginContent defines what the user actually sees on their screen.
 * It contains the text fields, buttons, and error messages.
 */
@Composable
fun LoginContent(
    authState: AuthState,
    onLogin: (String, String) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    // Temporary memory to hold what the user types
    var regNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Column stacks items vertically (top to bottom)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_large)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App Title
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = dimensionResource(id = R.dimen.text_size_extra_large).value.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        // Subtitle
        Text(
            text = stringResource(id = R.string.student_portal),
            fontSize = dimensionResource(id = R.dimen.text_size_medium).value.sp,
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_extra_large))
        )

        // Show an error message if the login fails
        if (authState is AuthState.Error) {
            Text(
                text = authState.message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_medium))
            )
        }

        // Show a success message if they just finished signing up
        if (authState is AuthState.RegisterSuccess) {
            Text(
                text = stringResource(id = R.string.registration_successful),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_medium))
            )
        }

        // Registration Number Input Field
        OutlinedTextField(
            value = regNumber,
            onValueChange = { regNumber = it },
            label = { Text(stringResource(id = R.string.reg_number)) },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))

        // Password Input Field (hides characters)
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(id = R.string.password)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_extra_large)))

        // Login Button
        Button(
            onClick = { onLogin(regNumber, password) },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            enabled = authState !is AuthState.Loading // Disable button while loading
        ) {
            if (authState is AuthState.Loading) {
                // Show a spinning wheel while the app checks credentials
                CircularProgressIndicator(
                    modifier = Modifier.size(dimensionResource(id = R.dimen.progress_indicator_size)),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = dimensionResource(id = R.dimen.progress_indicator_stroke)
                )
            } else {
                Text(
                    text = stringResource(id = R.string.login),
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))
                )
            }
        }

        // Signup link for new users
        TextButton(onClick = onNavigateToRegister) {
            Text(stringResource(id = R.string.no_account_signup))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    com.example.ndejjeconnect.ui.theme.NdejjeConnectTheme {
        LoginContent(
            authState = AuthState.Idle,
            onLogin = { _, _ -> },
            onNavigateToRegister = {}
        )
    }
}
