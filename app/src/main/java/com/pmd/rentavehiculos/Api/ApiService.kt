package com.pmd.rentavehiculos.Api

import com.pmd.rentavehiculos.Entity.Persona
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface ApiService{
    @GET("personas")
    suspend fun obtenerpersonas(@Header("x-llave-api") token : String) : List<Persona>;
}