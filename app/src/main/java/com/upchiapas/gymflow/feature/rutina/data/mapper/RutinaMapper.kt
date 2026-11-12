package com.upchiapas.gymflow.rutina.data.mapper

import com.upchiapas.gymflow.feature.rutina.data.dto.RutinaDto
import com.upchiapas.gymflow.feature.rutina.domain.model.Rutina

// Extensión para convertir de Red (DTO) a Interfaz (Dominio)
fun RutinaDto.toDomain(): Rutina {
    return Rutina(
        id = this.id ?: "",
        nombre = this.nombre,
        grupoMuscular = this.grupoMuscular,
        series = this.series,
        repeticiones = this.repeticiones,
        activa = this.activa,
        fechaCreacion = this.fechaCreacion
    )
}

// Extensión para convertir de Interfaz (Dominio) a Red (DTO) para enviar a la API
fun Rutina.toDto(): RutinaDto {
    return RutinaDto(
        id = if (this.id.isEmpty()) null else this.id,
        nombre = this.nombre,
        grupoMuscular = this.grupoMuscular,
        series = this.series,
        repeticiones = this.repeticiones,
        activa = this.activa,
        fechaCreacion = this.fechaCreacion
    )
}