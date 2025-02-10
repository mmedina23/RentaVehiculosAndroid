package com.pmd.rentavehiculos.Api

import com.pmd.rentavehiculos.Entity.Persona
import com.pmd.rentavehiculos.Entity.UserRequest
import com.pmd.rentavehiculos.Entity.Usuario
import com.pmd.rentavehiculos.Entity.Vehiculo
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService{
    @POST("auth/login")
    suspend fun login(@Body userRequest: UserRequest) : Response<Usuario>

    @GET("personas")
    suspend fun obtenerpersonas(@Header("x-llave-api") token : String) : List<Persona>;

    @GET("vehiculos")
    suspend fun obtenervehiculos(@Header("x-llave-api") token: String, @Query("estado") estado : String? = null) : List<Vehiculo>;
}