package com.pmd.rentavehiculos.data.model

data class Persona(
    val id: Int,
    val nombre: String,
    val apellido: String,
    val direccion: String,
    val telefono: String,
    val tipo_identificacion: String,
    val identificacion: String,
)