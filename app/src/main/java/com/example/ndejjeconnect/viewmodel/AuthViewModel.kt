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
 * Logic Layer: Manages Authentication and User Sessions.
 * Handles login, registration, and profile state.
 */
class AuthViewModel(private val repository: MainRepository) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun login(regNumber: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val user = repository.getUserByRegNumber(regNumber)
            if (user != null && user.password == password) {
                _currentUser.value = user
                _authState.value = AuthState.Success
            } else {
                _authState.value = AuthState.Error("Invalid registration number or password")
            }
        }
    }

    fun register(name: String, regNumber: String, password: String, level: String, course: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
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
                _authState.value = AuthState.Error("User with this registration number already exists")
            }
        }
    }

    fun logout() {
        _currentUser.value = null
        _authState.value = AuthState.Idle
    }

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
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    object RegisterSuccess : AuthState()
    data class Error(val message: String) : AuthState()
}
