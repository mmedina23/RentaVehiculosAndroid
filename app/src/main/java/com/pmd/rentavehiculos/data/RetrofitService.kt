package com.pmd.rentavehiculos.data

import android.media.session.MediaSession.Token
import com.pmd.rentavehiculos.data.model.LoginRequest
import com.pmd.rentavehiculos.data.model.LoginResponse
import com.pmd.rentavehiculos.data.model.Renta
import com.pmd.rentavehiculos.data.model.Vehiculo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitService {
    @GET("vehiculos")
    suspend fun listVehiculos(
        @Header("x-llava-api") token: String,
        @Query("estado") estado: String
    ): List<Vehiculo>
    @POST("auth/login") // Agregar el endpoint del login
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse
    @GET("vehiculos/rentas") // Ajusta este endpoint si es necesario
    suspend fun getRentasPorVehiculo(
        @Header("x-llava-api") token: String
    ): List<Renta>
}

object RetrofitServiceFactory{
    fun RetrofitService(): RetrofitService{
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(RetrofitService::class.java)
    }
}