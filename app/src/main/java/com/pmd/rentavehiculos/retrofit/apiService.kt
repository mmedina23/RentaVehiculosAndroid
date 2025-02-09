package com.pmd.rentavehiculos.retrofit




import com.pmd.rentavehiculos.modelos.LoginRequest
import com.pmd.rentavehiculos.modelos.LoginResponse
import com.pmd.rentavehiculos.modelos.LogoutResponse
import com.pmd.rentavehiculos.modelos.Persona
import com.pmd.rentavehiculos.modelos.Renta
import com.pmd.rentavehiculos.modelos.Vehiculo
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Endpoint para iniciar sesión
    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    // Endpoint para cerrar sesión
    @POST("auth/logout")
    suspend fun logout(@Header("x-llave-api") apiKey: String): LogoutResponse

    // Endpoint para obtener la lista de personas
    @GET("personas")
    suspend fun getPersonas(@Header("x-llave-api") apiKey: String): List<Persona>

    // Endpoint para obtener una persona por su ID
    @GET("personas/{id}")
    suspend fun getPersonaById(
        @Header("x-llave-api") apiKey: String,
        @Path("id") id: Int
    ): Persona

    // Endpoint para obtener los vehículos rentados por una persona
    @GET("personas/{id}/rentas")
    suspend fun getRentasByPersona(
        @Header("x-llave-api") apiKey: String,
        @Path("id") id: Int
    ): List<Renta>

    // Endpoint para obtener la lista de vehículos
    @GET("vehiculos")
    suspend fun getVehiculos(@Header("x-llave-api") apiKey: String): List<Vehiculo>

    // Endpoint para crear un nuevo vehículo
    @POST("vehiculos")
    suspend fun createVehiculo(
        @Header("x-llave-api") apiKey: String,
        @Body vehiculo: Vehiculo
    ): Vehiculo

    // Endpoint para actualizar un vehículo existente
    @PUT("vehiculos/{id}")
    suspend fun updateVehiculo(
        @Header("x-llave-api") apiKey: String,
        @Path("id") id: Int,
        @Body vehiculo: Vehiculo
    ): Vehiculo

    // Endpoint para eliminar un vehículo
    @DELETE("vehiculos/{id}")
    suspend fun deleteVehiculo(
        @Header("x-llave-api") apiKey: String,
        @Path("id") id: Int
    ): Response<Unit>

    // Endpoint para liberar un vehículo rentado
    @PATCH("vehiculos/{id}")
    suspend fun liberarVehiculo(
        @Header("x-llave-api") apiKey: String,
        @Path("id") id: Int
    ): Vehiculo

    // Endpoint para obtener el historial de rentas de un vehículo
    @GET("vehiculos/{id}/rentas")
    suspend fun getRentasByVehiculo(
        @Header("x-llave-api") apiKey: String,
        @Path("id") id: Int
    ): List<Renta>
}
