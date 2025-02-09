package com.pmd.rentavehiculos.model

data class AuthResponse(
    val llave: String,
    val perfil: String,
    val persona: Persona
)
