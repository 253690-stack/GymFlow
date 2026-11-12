package com.upchiapas.gymflow.feature.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequestDto(
    val correo: String,
    val contrasena: String,
    val nombre: String? = null // Opcional porque el Login no lo necesita, pero el Registro sí
)