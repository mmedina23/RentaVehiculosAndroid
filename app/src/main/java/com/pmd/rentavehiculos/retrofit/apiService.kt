package com.pmd.rentavehiculos.retrofit;

import com.pmd.rentavehiculos.modelos.*;
import retrofit2.Response;
import retrofit2.http.*;

interface ApiService {

    // Autenticación
    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("auth/login")
    suspend fun loginUser(@Body request: LoginRequest): LoginResponse;

    // Personas
    @GET("personas")
    suspend fun getPersonas(@Header("x-llave-api") apiKey: String): List<Persona>;

    @GET("personas/{id}")
    suspend fun getPersonaById(
        @Header("x-llave-api") apiKey: String,
        @Path("id") id: Int
    ): Persona;

    @GET("personas/{id}/rentas")
    suspend fun getRentasByPersona(
        @Header("x-llave-api") apiKey: String,
        @Path("id") id: Int
    ): List<Renta>;

    // Vehículos
    @GET("vehiculos")
    suspend fun getVehiculos(
        @Header("x-llave-api") apiKey: String,
        @Query("estado") estado: String? = null
    ): List<Vehiculo>;

    @POST("vehiculos")
    suspend fun createVehiculo(
        @Header("x-llave-api") apiKey: String,
        @Body vehiculo: Vehiculo
    ): Vehiculo;

    @GET("vehiculos/{id}")
    suspend fun getVehiculoById(
        @Header("x-llave-api") apiKey: String,
        @Path("id") id: Int
    ): Vehiculo;

    @PUT("vehiculos/{id}")
    suspend fun updateVehiculo(
        @Header("x-llave-api") apiKey: String,
        @Path("id") id: Int,
        @Body vehiculo: Vehiculo
    ): Vehiculo;

    @DELETE("vehiculos/{id}")
    suspend fun deleteVehiculo(
        @Header("x-llave-api") apiKey: String,
        @Path("id") id: Int
    ): Response<Unit>;

    @POST("vehiculos/liberar/{vehiculoId}")
    suspend fun liberarVehiculo(
        @Header("Authorization") apiKey: String,
        @Path("vehiculoId") vehiculoId: Int
    ): Response<Unit>
 // compruebo que retorne Response<Unit>




    @GET("vehiculos/{id}/rentas")
    suspend fun getRentasByVehiculo(
        @Header("x-llave-api") apiKey: String,
        @Path("id") id: Int
    ): List<Renta>;

    @POST("vehiculos/{id}/rentas")
    suspend fun rentarVehiculo(
        @Header("x-llave-api") apiKey: String,
        @Path("id") vehiculoId: Int,
        @Body renta: Renta
    ): Response<Unit>


    @PATCH("vehiculos/{id}")
    suspend fun entregarVehiculo(
        @Header("x-llave-api") apiKey: String,
        @Path("id") vehiculoId: Int
    );
}
