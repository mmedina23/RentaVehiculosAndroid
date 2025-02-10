package com.pmd.rentavehiculos.modelo

data class LoginResponse(
    val llave: String, // API Key
    val perfil: String, // ADMIN o CLIENTE
    val persona: Persona
)
