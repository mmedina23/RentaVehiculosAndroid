package com.pmd.rentavehiculos.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "http://192.168.1.11:8080/api/v1/" // ✅ Asegúrate de que termina en "/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)  // ✅ Usa la URL corregida
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}




