package com.pmd.rentavehiculos.classes

data class Vehiculo(
    val id: Int,
    val marca: String,
    val color: String,
    val carroceria: String,
    val plazas: Int,
    val cambios: String,
    val tipo_combustible: String,
    val valor_dia: Int,
    val disponible: Boolean,
    val imagen: String
)
