package com.pmd.rentavehiculos.data

import com.pmd.rentavehiculos.modelo.LoginResponse
import com.pmd.rentavehiculos.modelo.LoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthService {

    //autenticación
    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("auth/login")
    //suspend fun login(@Body request: LoginRequest): LoginResponse
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>//para manejar los errores http envuelvo en un response
}