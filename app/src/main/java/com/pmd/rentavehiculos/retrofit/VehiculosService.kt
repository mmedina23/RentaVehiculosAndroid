package com.pmd.rentavehiculos.retrofit


import com.pmd.rentavehiculos.modelo.RentaRequest
import com.pmd.rentavehiculos.modelo.Vehiculo
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
        @Header("x-llave-api") apiKey: String, // Encabezado obligatorio
        @Query("estado") estado: String = "disponibles" // Parámetro de consulta opcional con valor predeterminado
    ): List<Vehiculo>

    @GET("vehiculos/{id}/rentas")
    suspend fun obtenerHistorialRentas(
        @Header("x-llave-api") apiKey: String,
        @Path("id") vehiculoId: Int
    ): List<RentaRequest>

    @POST("vehiculos/{id}/rentas")
    suspend fun reservarVehiculo(
        @Header("x-llave-api") apiKey: String,
        @Path("id") vehiculoId: Int,
        @Body rentaRequest: RentaRequest
    )

    @GET("personas/{id}/rentas")
    suspend fun obtenerVehiculosRentados(
        @Header("x-llave-api") apiKey: String,
        @Path("id") personaId: Int
    ): List<RentaRequest>

    @PATCH("vehiculos/{id}")
    suspend fun entregarVehiculo(
        @Header("x-llave-api") apiKey: String,
        @Path("id") vehiculoId: Int
    )
}
