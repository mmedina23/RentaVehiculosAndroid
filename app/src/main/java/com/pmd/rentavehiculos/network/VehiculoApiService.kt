package com.pmd.rentavehiculos.network

import com.pmd.rentavehiculos.model.Vehiculo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
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

    @POST("vehiculos/{id}/rentas")
    suspend fun reservarVehiculo(
        @Path("id") idVehiculo: Int,  // ID del vehículo a rentar
        @Header("x-llave-api") llaveApi: String  // Llave API del usuario
    ): Response<Void>

}

