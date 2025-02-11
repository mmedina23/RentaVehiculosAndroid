package com.pmd.rentavehiculos.data.model

data class Persona (
    val id: String,
    val nombre: String,
    val apellidos: String,
    val edad: Int,
    val direccion: String,
    val telefono: Int,
    val tipo_identificacion: String,
    val identificacion: String
    )