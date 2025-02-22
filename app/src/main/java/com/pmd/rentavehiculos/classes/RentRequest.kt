package com.pmd.rentavehiculos.classes

import java.util.Date

data class RentRequest(
    val persona: Persona,
    val vehiculo: Vehiculo,
    val dias_renta: Int,
    val valor_total_renta: Int,
    val fecha_renta: String,
    val fecha_estimada_entrega: String,
    val fecha_entregado: String
)
