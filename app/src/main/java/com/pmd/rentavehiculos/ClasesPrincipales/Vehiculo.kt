package com.pmd.rentavehiculos.ClasesPrincipales

data class Vehiculo(
    val id: Int,
    val marca: String,
    val color: String,
    val carroceria: String,
    val plazas: Int,
    val cambios: String,
    val tipo_combustible: String,
    val valor_dia: Double,
    val disponible: Boolean
)
