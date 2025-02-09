package com.pmd.rentavehiculos.logic

import com.pmd.rentavehiculos.model.AuthResponse
import com.pmd.rentavehiculos.model.LoginRequest
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthService {
    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse
}



