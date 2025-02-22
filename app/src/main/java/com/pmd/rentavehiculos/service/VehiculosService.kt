package com.pmd.rentavehiculos.service

import com.pmd.rentavehiculos.classes.RentRequest
import com.pmd.rentavehiculos.classes.Vehiculo
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface VehiculosService {
    //lista todos los vehiculos disponibles
    @GET("vehiculos")
    suspend fun getVehiculos(
        @Header("x-llave-api") key: String,
        @Query("estado") estado: String = "disponibles"
        ): List<Vehiculo>

    //historial de renta de un vehiculo
    @GET("vehiculos/{id}/rentas")
    suspend fun getHistorial(
        @Header("x-llave-api") key: String,
        @Path("id") vhId: Int
    ): List<RentRequest>

    //vehiculos rentados por una persona
    @GET("personas/{id}/rentas")
    suspend fun getRentasPersona(
        @Header("x-llave-api") key: String,
        @Path("id") pId: Int
    ): List<RentRequest>

    //renta un vehiculo
    @POST("vehiculos/{id}/rentas")
    suspend fun rentarVehiculo(
        @Header("x-llave-api") key: String,
        @Path("id") vhId: Int,
        @Body request: RentRequest
    )

    //libera vehiculo rentado
    @PATCH("vehiculos/{id}")
    suspend fun liberarVehiculo(
        @Header("x-llave-api") key: String,
        @Path("id") vhId: Int,
    )
}