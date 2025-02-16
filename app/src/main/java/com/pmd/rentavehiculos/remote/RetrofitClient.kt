package com.pmd.rentavehiculos.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://192.168.1.11:8080/api/v1/"

    // ✅ Crear una única instancia de Retrofit
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // ✅ Servicios
    val authService: AuthService by lazy {
        retrofit.create(AuthService::class.java)
    }

    val vehiculosService: VehiculosService by lazy {
        retrofit.create(VehiculosService::class.java)
    }

    val usuariosService: UsuariosService by lazy {
        retrofit.create(UsuariosService::class.java)
    }
}
