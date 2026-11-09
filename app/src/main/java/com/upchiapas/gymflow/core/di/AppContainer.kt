package com.upchiapas.gymflow.core.di

import android.content.Context
import com.upchiapas.gymflow.core.data.remote.ApiClient
import com.upchiapas.gymflow.core.data.local.TokenStorage
import com.upchiapas.gymflow.core.data.local.TokenStorageImpl
import com.upchiapas.gymflow.core.data.remote.GymApiService
import com.upchiapas.gymflow.feature.auth.data.repository.AuthRepository
import com.upchiapas.gymflow.feature.auth.data.repository.AuthRepositoryImpl
import com.upchiapas.gymflow.feature.rutina.data.repository.RutinaRepository
import com.upchiapas.gymflow.feature.rutina.data.repository.RutinaRepositoryImpl

// Interfaces de repositorios en el siguiente commit

interface AppContainer {
    val tokenStorage: TokenStorage
    val authRepository: AuthRepository
    val rutinaRepository: RutinaRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    // Usamos 'by lazy' para que la instancia solo se cree la primera vez que se solicita
    override val tokenStorage: TokenStorage by lazy {
        TokenStorageImpl(context)
    }

    private val gymApiService: GymApiService by lazy {
        ApiClient.create(tokenStorage)
    }

    override val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(gymApiService, tokenStorage)
    }

    override val rutinaRepository: RutinaRepository by lazy {
        RutinaRepositoryImpl(gymApiService)
    }
}
