package com.upchiapas.gymflow.data.remote

import com.upchiapas.gymflow.feature.auth.data.dto.AuthRequestDto
import com.upchiapas.gymflow.feature.auth.data.dto.AuthResponseDto
import com.upchiapas.gymflow.feature.rutina.data.dto.RutinaDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface GymApiService {
    // Endpoints de Autenticación
    @POST("register")
    suspend fun register(@Body request: AuthRequestDto): AuthResponseDto

    @POST("login")
    suspend fun login(@Body request: AuthRequestDto): AuthResponseDto

    // Endpoints CRUD de Rutinas
    @GET("rutinas")
    suspend fun getRutinas(): List<RutinaDto>

    @GET("rutinas/{id}")
    suspend fun getRutinaById(@Path("id") id: String): RutinaDto

    @POST("rutinas")
    suspend fun createRutina(@Body rutina: RutinaDto): RutinaDto

    @PUT("rutinas/{id}")
    suspend fun updateRutina(@Path("id") id: String, @Body rutina: RutinaDto): RutinaDto

    @DELETE("rutinas/{id}")
    suspend fun deleteRutina(@Path("id") id: String)
}