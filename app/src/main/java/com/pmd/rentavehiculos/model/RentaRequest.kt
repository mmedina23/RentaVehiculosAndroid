package com.pmd.rentavehiculos.model

import java.util.Date

data class RentaRequest(
    val persona: Persona,
    val vehiculo: Vehiculo,
    val dias_renta: Int,
    val valor_total_renta: Double,
    val fecha_renta: String,
    val fecha_estimada_entrega: String,
    val fecha_entregado: String
)

