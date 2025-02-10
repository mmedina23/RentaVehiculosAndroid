package com.pmd.rentavehiculos.data.model

data class LoginResponse(
    val persona: Persona,
    val llave: String,
    val fecha_exp_llave: String
)
