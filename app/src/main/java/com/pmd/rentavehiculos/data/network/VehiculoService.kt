package com.pmd.rentavehiculos.data.network

import com.pmd.rentavehiculos.data.model.Renta
import com.pmd.rentavehiculos.data.model.Vehiculo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
interface VehiculoService {
    @GET("vehiculos")
    suspend fun obtenerVehiculos(
        @Header("x-llave-api") apiKey: String,
        @Query("estado") estado: String? = null
    ): Response<List<Vehiculo>>

    @PUT("vehiculos/{id}")
    suspend fun actualizarVehiculo(
        @Header("x-llave-api") apiKey: String,
        @Path("id") vehiculoId: Int,
        @Body vehiculo: Vehiculo
    ): Response<Vehiculo>

    // Obtener la lista de vehículoss (para admin)
    @GET("vehiculos")
    suspend fun obtenerVehiculosAdmin(
        @Header("x-llave-api") apiKey: String,
        @Query("estado") estado: String? = null
    ): Response<List<Vehiculo>>

    // Obtener el historial de rentas de un vehículo
    @GET("vehiculos/{id}/rentas")
    suspend fun obtenerHistorialRentas(
        @Header("x-llave-api") apiKey: String,
        @Path("id") vehiculoId: Int
    ): Response<List<Renta>>

    @DELETE("vehiculos/{id}")
    suspend fun eliminarVehiculo(
        @Header("x-llave-api") apiKey: String,
        @Path("id") vehiculoId: Int
    ): Response<Unit>

    @POST("vehiculos")
    suspend fun crearVehiculo(
        @Header("x-llave-api") apiKey: String,
        @Body vehiculo: Vehiculo
    ): Response<Vehiculo>

    @GET("vehiculos/{id}")
    suspend fun obtenerDetalleVehiculo(
        @Header("x-llave-api") apiKey: String,
        @Path("id") vehiculoId: Int
    ): Response<Vehiculo>
}
