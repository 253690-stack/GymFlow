package com.upchiapas.gymflow.core.di

import android.app.Application

class GymApplication : Application() {

    // Variable pública inmutable que expone el contenedor a toda la app
    lateinit var container: AppContainer
        private set // Solo esta clase puede asignarle un valor

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}