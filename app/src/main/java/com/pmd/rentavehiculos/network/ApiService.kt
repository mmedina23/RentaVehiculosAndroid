package com.pmd.rentavehiculos.network

import com.pmd.rentavehiculos.models.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // 🔹 Autenticación (Login)
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    // 🔹 Obtener lista de vehículos disponibles
    @GET("vehiculos")
    fun obtenerVehiculos(
        @Header("x-llave-api") token: String
    ): Call<List<Vehiculo>>

    // 🔹 Obtener lista de vehículos rentados
    @GET("vehiculos/rentados")
    fun obtenerVehiculosRentados(
        @Header("x-llave-api") token: String
    ): Call<List<VehiculoRentado>>

    // 🔹 Rentar un vehículo
    @POST("vehiculos/{id}/rentas")
    fun rentarVehiculo(
        @Header("x-llave-api") token: String,
        @Path("id") vehiculoId: Int,
        @Body request: Map<String, Any> // 🔹 Se usa un Map para enviar un JSON flexible
    ): Call<RentarVehiculoResponse>

    // 🔹 Devolver un vehículo rentado
    @PATCH("vehiculos/{id}/devolver")
    fun devolverVehiculo(
        @Header("x-llave-api") token: String,
        @Path("id") vehiculoId: Int
    ): Call<Void> // ✅ Retrofit devuelve un `Void` cuando no hay respuesta en el body








}
