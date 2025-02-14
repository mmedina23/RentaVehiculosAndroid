package com.pmd.rentavehiculos.data.repository

import com.pmd.rentavehiculos.data.model.Persona
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface PersonaService {
    // Recupera la lista de todas las personas registradas.
    @GET("personas")
    suspend fun obtenerPersonas(
        @Header("x-llave-api") apiKey: String
    ): Response<List<Persona>>

    // Recupera una persona por su identificador.
    @GET("personas/{id}")
    suspend fun obtenerPersona(
        @Header("x-llave-api") apiKey: String,
        @Path("id") personaId: Int
    ): Response<Persona>
}