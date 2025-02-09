package com.pmd.rentavehiculos.model

data class Usuario(
    val persona: Persona,
    val perfil: String,
    val llave: String,
    val fecha_exp_llave: String
)
