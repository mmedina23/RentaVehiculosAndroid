package com.pmd.rentavehiculos.data.model

data class AuthResponse(
    val llave: String,
    val perfil: String,
    val persona: Persona,
)