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
 * AuthViewModel: The "Security Guard & ID Manager".
 * 
 * Think of this as the person sitting at the university entrance.
 * - They check your ID (Login).
 * - They help new students sign the register (Register).
 * - They remember who is currently inside the building (Session Management).
 */
class AuthViewModel(private val repository: MainRepository) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    /**
     * Login: Checking the ID.
     * We ask the "Manager" (Repository) to find the student in the database.
     * If the password matches, we let them in.
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val user = repository.getUserByEmail(email)
            if (user != null && user.password == password) {
                _currentUser.value = user
                _authState.value = AuthState.Success
            } else {
                _authState.value = AuthState.Error("Invalid email or password")
            }
        }
    }

    /**
     * Register: Adding a new student to the books.
     * We check if the student is already registered. If not, we create a new "file" (User).
     */
    fun register(name: String, email: String, regNumber: String, password: String, level: String, course: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val existingUser = repository.getUserByEmail(email)
            if (existingUser == null) {
                val newUser = User(
                    regNumber = regNumber,
                    email = email,
                    name = name, 
                    password = password,
                    level = level,
                    course = course
                )
                repository.registerUser(newUser)
                _authState.value = AuthState.RegisterSuccess
            } else {
                _authState.value = AuthState.Error("User with this email already exists")
            }
        }
    }

    /**
     * Logout: Handing back the ID and leaving.
     */
    fun logout() {
        _currentUser.value = null
        _authState.value = AuthState.Idle
    }

    /**
     * Update Profile: Changing the name on your ID card.
     */
    fun updateProfile(name: String, profileImageUri: String?) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val updatedUser = user.copy(name = name, profileImageUri = profileImageUri)
            repository.updateUser(updatedUser)
            _currentUser.value = updatedUser
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }

    fun resetPassword(regNumber: String, email: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val user = repository.getUserByEmail(email)
            if (user != null && user.regNumber == regNumber) {
                _authState.value = AuthState.PasswordResetInitiated
            } else {
                _authState.value = AuthState.Error("User not found with provided registration number and email")
            }
        }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    object RegisterSuccess : AuthState()
    object PasswordResetInitiated : AuthState()
    data class Error(val message: String) : AuthState()
}
