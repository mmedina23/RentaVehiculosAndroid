package com.pmd.rentavehiculos.remote

import com.pmd.rentavehiculos.model.Vehiculo
import com.pmd.rentavehiculos.model.AuthResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(
    val nombre_usuario: String,
    val contrasena: String
)

interface ApiService {
    @GET("vehiculos/disponibles")
    fun obtenerVehiculosDisponibles(): Call<List<Vehiculo>>

    @GET("vehiculos/{id}")
    fun obtenerDetalleVehiculo(@Path("id") id: Int): Call<Vehiculo>

    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<AuthResponse>
}
