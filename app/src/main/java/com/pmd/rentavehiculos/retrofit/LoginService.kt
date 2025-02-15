package com.pmd.rentavehiculos.retrofit

import com.pmd.rentavehiculos.modelo.LoginRequest
import com.pmd.rentavehiculos.modelo.LoginResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginService {
    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}