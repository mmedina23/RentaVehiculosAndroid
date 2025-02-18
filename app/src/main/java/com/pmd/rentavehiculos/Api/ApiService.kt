package com.pmd.rentavehiculos.Api

import com.pmd.rentavehiculos.Entity.Persona
import com.pmd.rentavehiculos.Entity.Renta
import com.pmd.rentavehiculos.Entity.RentaAdmin
import com.pmd.rentavehiculos.Entity.RentaRequest
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
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService{
    @POST("auth/login")
    suspend fun login(@Body userRequest: UserRequest) : Response<Usuario>

    @GET("personas")
    suspend fun obtenerpersonas(@Header("x-llave-api") token : String) : List<Persona>;

    @GET("vehiculos")
    suspend fun obtenervehiculos(@Header("x-llave-api") token: String, @Query("estado") estado : String? = null) : List<Vehiculo>;

    @POST("vehiculos/{id}/rentas")
    suspend fun rentarVehiculo(
        @Path("id") vehiculoId: Int,
        @Header("x-llave-api") apiKey: String,
        @Body rentaRequest: RentaRequest
    ): Response<Unit>

    @GET("personas/{id}/rentas")
    suspend fun obtenerMisVehiculos(@Path("id") id:Int ,@Header("x-llave-api") token: String) : List<Renta>

    //Metodo para admind
    @GET("vehiculos/{id}/rentas")
    suspend fun obtenerDetalleVehiculo(@Path("id") id:Int, @Header("x-llave-api") token: String) : List<RentaAdmin>
}