package com.pmd.rentavehiculos.remote

import com.pmd.rentavehiculos.model.AuthResponse
import com.pmd.rentavehiculos.model.LoginSolicitud
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("auth/login")
    suspend fun login(@Body request: LoginSolicitud): AuthResponse
}

