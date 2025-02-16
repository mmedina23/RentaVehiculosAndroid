package com.pmd.rentavehiculos.network

import com.pmd.rentavehiculos.model.Vehiculo
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface VehiculoApiService {
    @GET("vehiculos")
    suspend fun obtenerVehiculos(
        @Header("x-llave-api") llaveApi: String
    ): List<Vehiculo>

    @GET("vehiculos/{id}/rentas")
    suspend fun obtenerVehiculosRentados(
        @Path("id") id: Int,
        @Header("x-llave-api") llaveApi: String
    ): List<Renta>


}

