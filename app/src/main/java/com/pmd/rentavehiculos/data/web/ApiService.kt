package com.pmd.rentavehiculos.data.web

import com.pmd.rentavehiculos.data.model.Vehiculo
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {
    @GET("vehiculos")
    suspend fun listaVehiculosDisponibles(
        @Header("x-llave-api") apiKey: String,
        @Query("estado") estado: String
    ): List<Vehiculo>
}
