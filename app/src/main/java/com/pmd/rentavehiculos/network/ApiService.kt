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


    @GET("vehiculos/rentados/{personaId}")
    fun obtenerVehiculosRentados(
        @Header("x-llave-api") token: String,
        @Path("personaId") personaId: Int  // ✅ Se agrega el parámetro
    ): Call<List<VehiculoRentado>>


    // 🔹 Devolver un vehículo rentado
    @PATCH("vehiculos/{id}/devolver")
    fun devolverVehiculo(
        @Header("x-llave-api") token: String,
        @Path("id") vehiculoId: Int
    ): Call<Void> // ✅ Retrofit devuelve un `Void` cuando no hay respuesta en el body

    // 🔹 Rentar un vehículo
    @POST("vehiculos/{vehiculoId}/rentar")  // ✅ Ahora la URL coincide con el Path
    fun rentarVehiculo(
        @Header("x-llave-api") token: String,
        @Path("vehiculoId") vehiculoId: Int,  // ✅ Ahora es igual a la URL
        @Body request: RentarVehiculoRequest
    ): Call<RentarVehiculoResponse>
}





