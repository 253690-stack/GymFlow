package com.upchiapas.gymflow.feature.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.upchiapas.gymflow.feature.auth.data.repository.AuthRepository
import com.upchiapas.gymflow.feature.auth.presentation.state.AuthUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    //Solo el ViewModel lo modifica (Privado)
    private val _uiState = MutableStateFlow(AuthUiState())

    // La UI solo puede leerlo
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun checkSession(): Boolean {
        return repository.hasValidSession()
    }

    fun login(correo: String, contrasena: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val success = repository.login(correo, contrasena)
                _uiState.update { it.copy(isLoading = false, isSuccess = success) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Error: Credenciales inválidas o falla de red") }
            }
        }
    }

    fun register(nombre: String, correo: String, contrasena: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val success = repository.register(nombre, correo, contrasena)
                _uiState.update { it.copy(isLoading = false, isSuccess = success) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Error al registrar usuario") }
            }
        }
    }

    fun logout() {
        repository.logout()
        _uiState.update { AuthUiState() } // Resetea el estado
    }
}

// Factory Manuel
class AuthViewModelFactory(private val repository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}