package com.pmd.rentavehiculos.models

data class LoginResponse(
    val llave: String, // API Key
    val perfil: String, // ADMIN o CLIENTE
    val persona: Persona
)
