package com.pmd.rentavehiculos.network

import com.pmd.rentavehiculos.models.DevolverVehiculoRequest
import com.pmd.rentavehiculos.models.DevolverVehiculoResponse
import com.pmd.rentavehiculos.models.LoginRequest
import com.pmd.rentavehiculos.models.LoginResponse
import com.pmd.rentavehiculos.models.RentaRequest
import com.pmd.rentavehiculos.models.RentaResponse
import com.pmd.rentavehiculos.models.Vehiculo
import com.pmd.rentavehiculos.models.VehiculoRentado
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiService {

    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
    @POST("vehiculos/devolver")
    fun devolverVehiculo(@Body request: DevolverVehiculoRequest): Call<DevolverVehiculoResponse>

    @GET("vehiculos/disponibles")
    fun getVehiculosDisponibles(): Call<List<Vehiculo>>
    @POST("vehiculos/rentar")
    fun rentarVehiculo(@Body request: RentaRequest): Call<RentaResponse>
    @GET("vehiculos/rentados")
    fun getVehiculosRentados(@Query("usuarioId") usuarioId: Int): Call<List<VehiculoRentado>>



}
