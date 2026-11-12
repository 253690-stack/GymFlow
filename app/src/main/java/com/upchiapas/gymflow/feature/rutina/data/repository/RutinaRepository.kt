package com.upchiapas.gymflow.feature.rutina.data.repository

import com.upchiapas.gymflow.core.data.remote.GymApiService
import com.upchiapas.gymflow.feature.rutina.domain.model.Rutina
import com.upchiapas.gymflow.rutina.data.mapper.toDomain
import com.upchiapas.gymflow.rutina.data.mapper.toDto

// La interfaz abstrae qué métodos existen
interface RutinaRepository {
    suspend fun getRutinas(): List<Rutina>
    suspend fun getRutinaById(id: String): Rutina
    suspend fun createRutina(rutina: Rutina): Rutina
    suspend fun updateRutina(rutina: Rutina): Rutina
    suspend fun deleteRutina(id: String)
}

// La implementación llama a la API y mapea los datos
class RutinaRepositoryImpl(
    private val apiService: GymApiService
) : RutinaRepository {

    override suspend fun getRutinas(): List<Rutina> {
        // Obtenemos DTOs de la red y los mapeamos a Dominio
        return apiService.getRutinas().map { it.toDomain() }
    }

    override suspend fun getRutinaById(id: String): Rutina {
        return apiService.getRutinaById(id).toDomain()
    }

    override suspend fun createRutina(rutina: Rutina): Rutina {
        val responseDto = apiService.createRutina(rutina.toDto())
        return responseDto.toDomain()
    }

    override suspend fun updateRutina(rutina: Rutina): Rutina {
        val responseDto = apiService.updateRutina(rutina.id, rutina.toDto())
        return responseDto.toDomain()
    }

    override suspend fun deleteRutina(id: String) {
        apiService.deleteRutina(id)
    }
}