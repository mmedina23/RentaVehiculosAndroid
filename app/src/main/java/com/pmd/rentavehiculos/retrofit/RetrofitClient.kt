package com.pmd.rentavehiculos.remote

import com.pmd.rentavehiculos.service.AuthService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://192.168.1.60:8080/api/v1/" // URL a la que se conecta Retrofit

    val authService: AuthService by lazy { //
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthService::class.java)
    }
}