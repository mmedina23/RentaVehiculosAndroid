package com.pmd.rentavehiculos.remote

import com.pmd.rentavehiculos.ClasesPrincipales.AuthResponse
import com.pmd.rentavehiculos.ClasesPrincipales.LoginRequest

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthService {
    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse
}
//SEGUN STACKOVERFLOW ESTO ES ASI. CHEQUEAR EN YOUTUBE. DE MOMENTO FUNCIONANDO
