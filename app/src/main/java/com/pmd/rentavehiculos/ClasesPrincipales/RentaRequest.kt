package com.pmd.rentavehiculos.ClasesPrincipales

data class RentaRequest(
    val persona: Persona,
    val vehiculo: Vehiculo,
    val dias_renta: Int,
    val valor_total_renta: Double,
    val fecha_renta: String,
    val fecha_estimada_entrega: String,
    val fecha_entrega: String
)
