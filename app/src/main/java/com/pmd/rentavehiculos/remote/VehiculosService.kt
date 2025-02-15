package com.pmd.rentavehiculos.remote


import com.pmd.rentavehiculos.model.RentaRequest
import com.pmd.rentavehiculos.model.Vehiculo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface VehiculosService {
    @GET("vehiculos")
    suspend fun obtenerVehiculos(
        @Header("x-llave-api") apiKey: String, // 🔥 Cambiado de Authorization a x-llave-api
        @Query("estado") estado: String = "disponibles"
    ): List<Vehiculo>


    @POST("vehiculos/{id}/rentas")
    suspend fun reservarVehiculo(
        @Header("x-llave-api") apiKey: String,  // 🔥 Cambiado también aquí
        @Path("id") vehiculoId: Int,
        @Body rentaRequest: RentaRequest
    ): Response<Unit>

    @GET("personas/{id}/rentas")
    suspend fun obtenerVehiculosRentados(
        @Header("x-llave-api") apiKey: String,  // 🔥 También corregido aquí
        @Path("id") personaId: Int
    ): List<RentaRequest>


    @GET("vehiculos/{id}/rentas")
    suspend fun obtenerHistorialRentas(
        @Header("x-llave-api") apiKey: String,
        @Path("id") vehiculoId: Int
    ): List<RentaRequest>


    @PATCH("vehiculos/{id}")
    suspend fun entregarVehiculo(
        @Header("x-llave-api") apiKey: String,
        @Path("id") vehiculoId: Int
    ): Response<Unit>


}


