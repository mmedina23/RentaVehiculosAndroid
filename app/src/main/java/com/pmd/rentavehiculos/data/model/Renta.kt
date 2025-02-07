package com.pmd.rentavehiculos.data.model

data class Renta(
    val id: Int,
    val personaId: Int,
    val vehiculoId: Int,
    val dias_renta: Int,
    val valor_total_venta: Double,
    val fecha_renta: String,
    val fecha_estimada_entrega: String,
    val fecha_entregado: String?  // Puede ser nulo si no se ha entregado
)
