package com.pmd.rentavehiculos.modelo

data class Vehiculo(
    val id: Int,
    val marca: String,
    val modelo: String,
    val disponible: Boolean,
    val color: String,
    val precioDia: Double
)
