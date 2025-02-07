package com.pmd.rentavehiculos.modelos

// Persona.kt
data class Persona(
    val id: Int,
    val nombre: String,
    val apellidos: String,
    val direccion: String,
    val telefono: String,
    val tipo_identificacion: String,
    val identificacion: String
)
