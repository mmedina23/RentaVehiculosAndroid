package com.pmd.rentavehiculos.dataModel

data class LoginResponse(
    val llave: String, // API Key
    val rol: String, // ADMIN o CLIENTE
    val persona: Persona
)
