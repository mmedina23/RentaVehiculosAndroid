package com.pmd.rentavehiculos.Entity

data class Renta(
    val id: Int,
    val vehiculo: Vehiculo,
    val dias_renta: Int,
    val valor_total_renta: Double,
    val fecha_renta: String,
    val fecha_estimada_entrega: String,
    val fecha_entregado: String?
)