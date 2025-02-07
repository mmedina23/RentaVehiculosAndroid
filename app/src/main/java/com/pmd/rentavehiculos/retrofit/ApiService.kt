package com.pmd.rentavehiculos.retrofit

import com.pmd.rentavehiculos.dataModel.LoginRequest
import com.pmd.rentavehiculos.dataModel.LoginResponse
import com.pmd.rentavehiculos.dataModel.RentaRequest
import com.pmd.rentavehiculos.dataModel.Vehiculo
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.Path
import retrofit2.Response

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("vehiculos")
    suspend fun getVehiculos(): Response<List<Vehiculo>>

    @GET("vehiculos/{id}")
    suspend fun getVehiculoById(@Path("id") id: Int): Response<Vehiculo>

    @POST("vehiculos/{id}/rentas")
    suspend fun rentarVehiculo(
        @Path("id") id: Int,
        @Body rentaRequest: RentaRequest
    ): Response<Unit>
}