package com.pmd.rentavehiculos.models



data class LoginResponse(
    val llave: String,
    val perfil: String,
    val personaId: Int // ✅ Añadimos `personaId`
)
