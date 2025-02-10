package com.pmd.rentavehiculos.service

import com.pmd.rentavehiculos.classes.LogRequest
import com.pmd.rentavehiculos.classes.LogResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface AuthService {
    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("auth/login")
    suspend fun login(@Body request: LogRequest): LogResponse
}