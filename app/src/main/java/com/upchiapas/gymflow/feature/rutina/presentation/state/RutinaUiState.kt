package com.upchiapas.gymflow.feature.rutina.presentation.state

import com.upchiapas.gymflow.feature.rutina.domain.model.Rutina

data class RutinaUiState(
    val isLoading: Boolean = false,
    val rutinas: List<Rutina> = emptyList(), // Lista de rutinas para el Home
    val errorMessage: String? = null,
    val operacionExitosa: Boolean = false // Para saber si se guardó/borró correctamente
)