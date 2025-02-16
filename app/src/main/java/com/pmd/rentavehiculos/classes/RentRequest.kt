package com.pmd.rentavehiculos.classes

import java.util.Date

data class RentRequest(
    val persona: Persona,
    val vehiculo: Vehiculo,
    val dias_renta: Int,
    val valor_total_renta: Int,
    val fecha_renta: Date,
    val fecha_estimada_entrega: Date
)
