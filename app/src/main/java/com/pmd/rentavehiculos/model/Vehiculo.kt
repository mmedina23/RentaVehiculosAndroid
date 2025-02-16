package com.pmd.rentavehiculos.model

data class Vehiculo(
    val id: Int,
    val marca: String,
    val modelo: String,
    val año: Int,
    val carroceria: String,
    val color: String,
    val disponible: Boolean,
    val valorDia: Double,
    val imagen: String
)

