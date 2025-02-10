package com.pmd.rentavehiculos.ClasesPrincipales

data class AuthResponse(
    val llave: String, // API Key
    val perfil: String, // ADMIN o CLIENTE
    val persona: Persona
)
