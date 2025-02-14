package com.pmd.rentavehiculos.data.repository

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/api/v1/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authService: AuthService by lazy {
        retrofit.create(AuthService::class.java)
    }

    val vehiculoService: VehiculoService by lazy {
        retrofit.create(VehiculoService::class.java)
    }

    val rentaService: RentaService by lazy {
        retrofit.create(RentaService::class.java)
    }
}
