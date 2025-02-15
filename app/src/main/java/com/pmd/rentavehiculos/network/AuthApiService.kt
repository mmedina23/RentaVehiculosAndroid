package com.pmd.rentavehiculos.network

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.Response

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @POST("auth/logout")
    suspend fun logout(
        @Body logoutRequest:LogoutRequest
    ): Response<Void> // El logout no requiere cuerpo en este caso
}
