package com.pmd.rentavehiculos.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // ipconfig: Cambiar la Url de casa y la de clase

    private const val BASE_URL = "http://192.168.211.1:8080/api/v1/" //URL CASA
    /*private const val BASE_URL = "http://10.221.85.106:8080/api/v1/"//URL CLASE*/

    val authService: AuthService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthService::class.java)
    }

    val vehiculosService: VehiculosService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VehiculosService::class.java)
    }
}
