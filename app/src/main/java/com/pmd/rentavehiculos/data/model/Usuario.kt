package com.pmd.rentavehiculos.data.model

import java.time.LocalDateTime

data class Usuario(
    val id: Int,
    val usuario: String,
    val contrasena: String,
    val perfil: String,
    val llave: String,
    val fechaExpLlave : LocalDateTime
)