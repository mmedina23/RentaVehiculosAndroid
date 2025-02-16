package com.pmd.rentavehiculos.remote

import com.pmd.rentavehiculos.model.Persona
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface UsuariosService {
    @GET("personas/{id}")
    suspend fun obtenerPersonaPorId(
        @Path("id") idCliente: Int,
        @Header("x-llave-api") apiKey: String
    ): Persona
}
