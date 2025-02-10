package com.pmd.rentavehiculos.retrofit

import com.pmd.rentavehiculos.dataModel.Vehiculo
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface VehiculosService {
    @GET("vehiculos")
    suspend fun obtenerVehiculos(
        @Header("x-llave-api") apiKey: String, // Encabezado obligatorio
        @Query("estado") estado: String = "disponibles" // Parámetro de consulta opcional con valor predeterminado
    ): List<Vehiculo>
}
