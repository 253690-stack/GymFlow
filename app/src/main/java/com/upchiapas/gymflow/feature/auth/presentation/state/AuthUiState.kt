package com.upchiapas.gymflow.feature.auth.presentation.state

data class AuthUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)