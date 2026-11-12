package com.upchiapas.gymflow.feature.rutina.domain.model

data class Rutina(
    val id: String = "", // El ID viene vacío al crear, pero la API lo llenará
    val nombre: String,
    val grupoMuscular: String,
    val series: Int,
    val repeticiones: Int,
    val activa: Boolean,
    val fechaCreacion: String
)