package com.tuusuario.gymflow.core.data.remote

import retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.upchiapas.gymflow.core.data.local.TokenStorage
import com.upchiapas.gymflow.data.remote.GymApiService
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object ApiClient {
    // IP que usa el Emulador para acceder a mi PC
    private const val BASE_URL = "http://10.0.2.2:3000/"

    fun create(tokenStorage: TokenStorage): GymApiService {
        // Interceptor que inyecta el token automáticamente si existe
        val authInterceptor = Interceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
            tokenStorage.getToken()?.let { token ->
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }
            chain.proceed(requestBuilder.build())
        }

        // Interceptor para ver los logs en el Logcat (útil para depurar)
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

        val json = Json { ignoreUnknownKeys = true } // Evita crasheos si la API manda datos extra
        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(GymApiService::class.java)
    }
}