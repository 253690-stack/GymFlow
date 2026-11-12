package com.upchiapas.gymflow.feature.auth.data.repository

import com.upchiapas.gymflow.core.data.local.TokenStorage
import com.upchiapas.gymflow.core.data.remote.GymApiService
import com.upchiapas.gymflow.feature.auth.data.dto.AuthRequestDto

interface AuthRepository {
    suspend fun login(correo: String, contrasena: String): Boolean
    suspend fun register(nombre: String, correo: String, contrasena: String): Boolean
    fun logout()
    fun hasValidSession(): Boolean
}

class AuthRepositoryImpl(
    private val apiService: GymApiService,
    private val tokenStorage: TokenStorage
) : AuthRepository {

    override suspend fun login(correo: String, contrasena: String): Boolean {
        val response = apiService.login(AuthRequestDto(correo, contrasena))
        tokenStorage.saveToken(response.token) // Guardamos el token en SharedPreferences
        return true
    }

    override suspend fun register(nombre: String, correo: String, contrasena: String): Boolean {
        val response = apiService.register(AuthRequestDto(correo, contrasena, nombre))
        tokenStorage.saveToken(response.token)
        return true
    }

    override fun logout() {
        tokenStorage.clearToken()
    }

    override fun hasValidSession(): Boolean {
        return tokenStorage.getToken() != null
    }
}