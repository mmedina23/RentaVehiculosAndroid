package com.pmd.rentavehiculos.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface VehiculoApiService {

    @GET("vehiculos")
    suspend fun obtenerVehiculos(
        @Header("x-llave-api") llaveApi: String, // Header obligatorio
        @Query("estado") estado: String? = null // Parámetro opcional
    ): List<Vehiculo>
}
