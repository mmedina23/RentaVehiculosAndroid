package com.pmd.rentavehiculos.modelo

data class Persona(
    val id: Int,
    val nombre: String,
    val apellidos: String,
    val direccion: String,
    val telefono: String,
    val tipoidentificacion: String,
    val identificacion: String
)
