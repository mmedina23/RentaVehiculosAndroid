package com.pmd.rentavehiculos.network

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @POST("auth/logout")
    fun logout(): Call<Void> // El logout no requiere cuerpo en este caso
}
