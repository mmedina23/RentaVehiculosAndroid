package com.pmd.rentavehiculos.data.model

data class AuthResponse(
    val llave: String, // API Key
    val perfil: String, // ADMIN o CLIENTE
    val persona: Persona
)