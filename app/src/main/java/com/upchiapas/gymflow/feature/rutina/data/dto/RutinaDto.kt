package com.upchiapas.gymflow.feature.rutina.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class RutinaDto(
    val id: String? = null,
    val nombre: String,
    val grupoMuscular: String,
    val series: Int,
    val repeticiones: Int,
    val activa: Boolean,
    val fechaCreacion: String
)