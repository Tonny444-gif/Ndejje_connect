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
 * View Layer: Authentication (Login Screen)
 * Provides a simple interface for students to log in.
 */
@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            onLoginSuccess()
            viewModel.resetState()
        }
    }

    LoginContent(
        authState = authState,
        onLogin = { reg, pass -> viewModel.login(reg, pass) },
        onNavigateToRegister = onNavigateToRegister
    )
}

@Composable
fun LoginContent(
    authState: AuthState,
    onLogin: (String, String) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var regNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_large)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = dimensionResource(id = R.dimen.text_size_extra_large).value.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(id = R.string.student_portal),
            fontSize = dimensionResource(id = R.dimen.text_size_medium).value.sp,
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_extra_large))
        )

        if (authState is AuthState.Error) {
            Text(
                text = authState.message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_medium))
            )
        }

        OutlinedTextField(
            value = regNumber,
            onValueChange = { regNumber = it },
            label = { Text(stringResource(id = R.string.reg_number)) },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(id = R.string.password)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_extra_large)))

        Button(
            onClick = { onLogin(regNumber, password) },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            enabled = authState !is AuthState.Loading
        ) {
            if (authState is AuthState.Loading) {
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
