package com.pmd.rentavehiculos.data

import com.pmd.rentavehiculos.modelo.LoginResponse
import com.pmd.rentavehiculos.modelo.LoginRequest
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthService {

    //autenticación
    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}