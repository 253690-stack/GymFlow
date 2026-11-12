package com.upchiapas.gymflow.feature.rutina.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.upchiapas.gymflow.feature.rutina.data.repository.RutinaRepository
import com.upchiapas.gymflow.feature.rutina.domain.model.Rutina
import com.upchiapas.gymflow.feature.rutina.presentation.state.RutinaUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RutinaViewModel(private val repository: RutinaRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(RutinaUiState())
    val uiState: StateFlow<RutinaUiState> = _uiState.asStateFlow()

    init {
        // Al instanciar el ViewModel, cargamos la lista (El Read del CRUD)
        loadRutinas()
    }

    fun loadRutinas() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, operacionExitosa = false) }
            try {
                val lista = repository.getRutinas()
                _uiState.update { it.copy(isLoading = false, rutinas = lista) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Error al cargar rutinas: ${e.message}") }
            }
        }
    }

    fun createRutina(rutina: Rutina) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                repository.createRutina(rutina)
                //Marcamos el exito para que regrese la UI
                _uiState.update { it.copy(isLoading = false, operacionExitosa = true) }
                //Recargamos en segundo plano
                val listaActualizada = repository.getRutinas()
                _uiState.update { it.copy(rutinas = listaActualizada) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Error al crear: ${e.message}") }
            }
        }
    }

    fun updateRutina(rutina: Rutina) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                repository.updateRutina(rutina)
                //Marcamos exito para que la UI regrese
                _uiState.update { it.copy(isLoading = false, operacionExitosa = true) }
                //Actualizamos la lista
                val listaActualizada = repository.getRutinas()
                _uiState.update { it.copy(rutinas = listaActualizada) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Error al actualizar: ${e.message}") }
            }
        }
    }

    fun deleteRutina(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                repository.deleteRutina(id)
                _uiState.update { it.copy(isLoading = false, operacionExitosa = true) }
                loadRutinas() // Recargamos la lista tras borrar
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Error al eliminar") }
            }
        }
    }

    fun resetOperacion() {
        _uiState.update { it.copy(operacionExitosa = false, errorMessage = null) }
    }
}

class RutinaViewModelFactory(private val repository: RutinaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RutinaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RutinaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}