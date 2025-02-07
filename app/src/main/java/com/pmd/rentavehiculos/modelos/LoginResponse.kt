package com.pmd.rentavehiculos.modelos

// LoginResponse.kt
data class LoginResponse(
    val persona: Persona,
    val perfil: String,
    val llave: String,
    val fecha_exp_llave: String
)
