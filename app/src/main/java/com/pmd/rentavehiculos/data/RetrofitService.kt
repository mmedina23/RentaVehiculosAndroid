package com.pmd.rentavehiculos.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitServiceFactory {

    private const val BASE_URL = "http://10.0.2.2:8080/api/v1/"

    // Crear instancia del cliente una sola vez
    private val client: OkHttpClient by lazy {
        provideClient()
    }

    fun vehiculoService(): VehiculoService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL) // parte de la URL que es fija
            .addConverterFactory(GsonConverterFactory.create()) // convierte el resultado de la petición a objeto
            .client(client)
            .build()
            .create(VehiculoService::class.java)
    }

    fun personaService(): PersonaService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(PersonaService::class.java)
    }

    fun authService(): AuthService{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(AuthService::class.java)
    }


    private fun provideClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor() // Está pendiente de lo que entra y sale
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .retryOnConnectionFailure(true)
            .build()
    }
}
