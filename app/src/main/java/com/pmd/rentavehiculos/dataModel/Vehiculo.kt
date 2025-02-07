package com.pmd.rentavehiculos.dataModel

data class Vehiculo(
    val id: Int,
    val marca: String,
    val modelo: String,
    val disponible: Boolean,
    val precioPorDia: Double
)
