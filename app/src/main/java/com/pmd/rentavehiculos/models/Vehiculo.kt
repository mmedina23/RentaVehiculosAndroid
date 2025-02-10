package com.pmd.rentavehiculos.models

data class Vehiculo(
    val id: Int,
    val marca: String,
    val modelo: String,
    val precioPorDia: Double,
    val imagenUrl: String
)
