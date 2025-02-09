package com.pmd.rentavehiculos.data

import com.pmd.rentavehiculos.data.model.AutenticacionDto
import com.pmd.rentavehiculos.data.model.LoginRequestDto
import com.pmd.rentavehiculos.data.model.LogoutRequestDto
import com.pmd.rentavehiculos.data.model.Persona
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {
    @POST("/auth/login")
    suspend fun login(@Body loginRequestDto: LoginRequestDto): Response<AutenticacionDto>

    @POST("/auth/logout")
    suspend fun logout(@Body logoutRequestDto: LogoutRequestDto): Response<Void>
}