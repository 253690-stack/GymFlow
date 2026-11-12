package com.upchiapas.gymflow.feature.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponseDto(
    val token: String,
    val usuarioId: String? = null
)