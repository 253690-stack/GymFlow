package com.upchiapas.gymflow.core.di

import android.content.Context
import com.upchiapas.gymflow.core.data.local.TokenStorage
import com.upchiapas.gymflow.core.data.local.TokenStorageImpl

// Interfaces de repositorios en el siguiente commit

interface AppContainer {
    val tokenStorage: TokenStorage
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    // Usamos 'by lazy' para que la instancia solo se cree la primera vez que se solicita
    override val tokenStorage: TokenStorage by lazy {
        TokenStorageImpl(context)
    }
}
