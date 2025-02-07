package com.pmd.rentavehiculos.data.model

data class Vehiculo(
    val id: String,
    val marca: String,
    val color: String,
    val carroceria : String,
    val plazas: Int,
    val cambios: String,
    val tipo_combustible: String,
    val valorDia: Int,
    val disponible : Boolean
)
