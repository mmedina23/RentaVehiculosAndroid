package com.pmd.rentavehiculos.data.network

import com.pmd.rentavehiculos.data.model.LoginRequest
import com.pmd.rentavehiculos.data.model.LoginResponse
import com.pmd.rentavehiculos.data.model.Persona
import com.pmd.rentavehiculos.data.model.Renta
import com.pmd.rentavehiculos.data.model.Vehiculo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.*

interface ApiService {

    // 🔹 Autenticación
    @POST("auth/login")
    suspend fun login(@Body credentials: LoginRequest): Response<LoginResponse>

    @POST("auth/logout")
    suspend fun logout(): Response<Void>

    // 🔹 Personas
    @GET("personas")
    suspend fun getPersonas(): Response<List<Persona>>

    @GET("personas/{id}")
    suspend fun getPersonaById(@Path("id") id: Int): Response<Persona>

    // 🔹 Vehículos
    @GET("vehiculos")
    suspend fun getVehiculos(): Response<List<Vehiculo>>

    @GET("vehiculos/{id}")
    suspend fun getVehiculoById(@Path("id") id: Int): Response<Vehiculo>

    @PUT("vehiculos/{id}")
    suspend fun actualizarVehiculo(@Path("id") id: Int, @Body vehiculo: Vehiculo): Response<Vehiculo>

    @PATCH("vehiculos/{id}")
    suspend fun modificarVehiculo(@Path("id") id: Int, @Body cambios: Map<String, Any>): Response<Vehiculo>

    @GET("vehiculos/{id}/rentas")
    suspend fun getRentasPorVehiculo(@Path("id") id: Int): Response<List<Renta>>
}

