package com.pmd.rentavehiculos.retrofit

import com.pmd.rentavehiculos.dataModel.LoginRequest
import com.pmd.rentavehiculos.dataModel.LoginResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    //    @POST("auth/login")
//    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
//
//    @GET("vehiculos")
//    suspend fun getVehiculos(): Response<List<Vehiculo>>
//
//    @GET("vehiculos/{id}")
//    suspend fun getVehiculoById(@Path("id") id: Int): Response<Vehiculo>
//
//    @POST("vehiculos/{id}/rentas")
//    suspend fun rentarVehiculo(
//        @Path("id") id: Int,
//        @Body rentaRequest: RentaRequest
//    ): Response<Unit>
    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}