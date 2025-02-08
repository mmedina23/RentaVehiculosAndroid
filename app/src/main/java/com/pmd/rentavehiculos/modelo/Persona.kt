package com.pmd.rentavehiculos.modelo

data class Persona(
    val id: Int,
    val nombre: String,
    val apellidos: String,
    val docIdent: String,
    val identificacion: String,
    val direccion: String,
    val telf: String
)
