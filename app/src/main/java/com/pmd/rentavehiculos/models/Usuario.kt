package com.pmd.rentavehiculos.models

data class Usuario(
    val persona: Persona,
    val perfil: String,
    val llave: String,
    val fecha_exp_llave: String
)
