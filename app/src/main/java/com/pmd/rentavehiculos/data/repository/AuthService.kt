package com.pmd.rentavehiculos.data.repository

import com.pmd.rentavehiculos.data.model.LoginRequest
import com.pmd.rentavehiculos.data.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse
}
