package com.pmd.rentavehiculos.data
import com.pmd.rentavehiculos.modelo.RentaVehiculo
import com.pmd.rentavehiculos.modelo.Vehiculo
import com.pmd.rentavehiculos.modelo.RentarVehiculoRequest
import com.pmd.rentavehiculos.modelo.RentarVehiculoResponse
import retrofit2.Response
import retrofit2.http.*

interface VehiculosService {

    // Obtener la lista de vehículos (por defecto solo los disponibles)
    @GET("vehiculos")
    suspend fun obtenerVehiculos(
        @Header("x-llave-api") apiKey: String,
        @Query("estado") estado: String = "disponibles"
    ): Response<List<Vehiculo>>

    // Reservar (rentar) un vehículo
    @POST("vehiculos/{id}/rentas")
    suspend fun reservarVehiculo(
        @Header("x-llave-api") apiKey: String,
        @Path("id") vehiculoId: Int,
        @Body rentaRequest: RentarVehiculoRequest
    ): Response<RentarVehiculoResponse>

    // Obtener la lista de vehículos rentados por una persona
    @GET("personas/{id}/rentas")
    suspend fun obtenerVehiculosRentados(
        @Header("x-llave-api") apiKey: String,
        @Path("id") personaId: Int
    ): Response<List<RentaVehiculo>>

    // Entregar (liberar) un vehículo
    @PATCH("vehiculos/{id}")
    suspend fun entregarVehiculo(
        @Header("x-llave-api") apiKey: String,
        @Path("id") vehiculoId: Int
    ): Response<Unit>

    // Actualizar los datos de un vehículo
    @PUT("vehiculos/{id}")
    suspend fun actualizarVehiculo(
        @Header("x-llave-api") apiKey: String,
        @Path("id") vehiculoId: Int,
        @Body vehiculo: Vehiculo
    ): Response<Vehiculo>

    // Eliminar un vehículo
    @DELETE("vehiculos/{id}")
    suspend fun eliminarVehiculo(
        @Header("x-llave-api") apiKey: String,
        @Path("id") vehiculoId: Int
    ): Response<Unit>

    // Crear un nuevo vehículo
    @POST("vehiculos")
    suspend fun crearVehiculo(
        @Header("x-llave-api") apiKey: String,
        @Body vehiculo: Vehiculo
    ): Response<Vehiculo>

    // Obtener el historial de rentas de un vehículo
    @GET("vehiculos/{id}/rentas")
    suspend fun obtenerHistorialRentas(
        @Header("x-llave-api") apiKey: String,
        @Path("id") vehiculoId: Int
    ): Response<List<RentaVehiculo>>
}