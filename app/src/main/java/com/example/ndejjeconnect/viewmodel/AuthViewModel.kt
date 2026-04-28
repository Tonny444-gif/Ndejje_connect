package com.example.ndejjeconnect.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ndejjeconnect.data.MainRepository
import com.example.ndejjeconnect.data.local.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * AuthViewModel handles authentication logic, including login, registration,
 * and user session management.
 */
class AuthViewModel(private val repository: MainRepository) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    /**
     * Authenticates a user with the provided registration number and password.
     */
    fun login(regNumber: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val user = repository.getUserByRegNumber(regNumber)
                if (user != null && user.password == password) {
                    _currentUser.value = user
                    _authState.value = AuthState.Success
                } else {
                    _authState.value = AuthState.Error("Invalid credentials provided.")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("An unexpected error occurred during login.")
            }
        }
    }

    /**
     * Registers a new user account in the system.
     */
    fun register(
        name: String, 
        regNumber: String, 
        password: String, 
        level: String, 
        course: String
    ) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val existingUser = repository.getUserByRegNumber(regNumber)
                if (existingUser == null) {
                    val newUser = User(
                        regNumber = regNumber,
                        name = name,
                        password = password,
                        level = level,
                        course = course
                    )
                    repository.registerUser(newUser)
                    _authState.value = AuthState.RegisterSuccess
                } else {
                    _authState.value = AuthState.Error("Account already exists for this registration number.")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Registration failed. Please try again.")
            }
        }
    }

    /**
     * Terminates the current user session.
     */
    fun logout() {
        _currentUser.value = null
        _authState.value = AuthState.Idle
    }

    /**
     * Updates the profile information for the currently authenticated user.
     */
    fun updateProfile(name: String, profileImageUri: String?) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            try {
                val updatedUser = user.copy(
                    name = name, 
                    profileImageUri = profileImageUri
                )
                repository.updateUser(updatedUser)
                _currentUser.value = updatedUser
            } catch (e: Exception) {
                // Potential error handling for profile updates
            }
        }
    }

    /**
     * Resets the authentication state to Idle.
     */
    fun resetState() {
        _authState.value = AuthState.Idle
    }
}

/**
 * Represents the various states of the authentication process.
 */
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    object RegisterSuccess : AuthState()
    data class Error(val message: String) : AuthState()
}
