package com.pmd.rentavehiculos.data.network

import com.pmd.rentavehiculos.data.model.LoginRequest
import com.pmd.rentavehiculos.data.model.LoginResponse
import com.pmd.rentavehiculos.data.model.LogoutRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse


    @POST("auth/logout")
    suspend fun logout(
        @Body request: LogoutRequest
    ): Response<Void>

}
