package com.pmd.rentavehiculos.data

import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitService {
    @GET("vehiculos")
    suspend fun lisVehiculos(
        @HEADER("x-llave-api") token: String,
        @QUERY("estado") estado: String,
    ): List<Vehiculo>
}

object RetrofitServiceFactory{
    fun RetrofitService(): RetrofitService{
        return Retrofit.Builder()
            .baseURL("http://10.0.2:8080/api/vi")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(RetrofitService::class.java)
    }
}

class RetrofitService {
}