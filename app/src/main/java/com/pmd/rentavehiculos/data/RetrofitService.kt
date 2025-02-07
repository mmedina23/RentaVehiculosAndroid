package com.pmd.rentavehiculos.data

import android.media.session.MediaSession.Token
import com.pmd.rentavehiculos.data.model.Vehiculo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitService {
    @GET("vehiculos")
    suspend fun listVehiculos(
        @Header("x-llava-api") token: String,
        @Query("estado") estado: String
    ): List<Vehiculo>
}

object RetrofitServiceFactory{
    fun RetrofitService(): RetrofitService{
        return Retrofit.Builder()
            .baseUrl("http://10.0.2:8080/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(RetrofitService::class.java)
    }
}