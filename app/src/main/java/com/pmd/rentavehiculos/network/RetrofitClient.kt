package com.pmd.rentavehiculos.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://localhost:8085/api/v1" // bueno esto lo obtuve cuando
    // ejecute el programa RentaVehiculosApi y así puedo ver la API con el swagger.

    val apiService: VehiculoApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // esto es como el Stringify
            // cuando convertimos JSON a objeto, pero en este caso objetos para Kotlin.
            .build()
            .create(VehiculoApiService::class.java)
    }
}
