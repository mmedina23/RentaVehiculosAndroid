package com.pmd.rentavehiculos.model

data class RentaRequest(
    val persona: Persona,  // ✅ Ahora tendrá `nombre` y `apellidos`
    val vehiculo: Vehiculo,
    val dias_renta: Int,
    val valor_total_renta: Int,
    val fecha_renta: String,
    val fecha_estimada_entrega: String
)
