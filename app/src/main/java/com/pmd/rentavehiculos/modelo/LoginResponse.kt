package com.pmd.rentavehiculos.modelo

data class LoginResponse(
    // respuesta del login incluirá el token, el perfil y la persona
    val token: String,
    val perfil: String,
    val persona: Persona
)

