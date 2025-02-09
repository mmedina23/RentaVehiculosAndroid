package com.pmd.rentavehiculos.data.model

import java.time.LocalDateTime

data class Renta (

    val id: Int,
    val persona: Persona,
    val vehiculo: Vehiculo,
    val diasRenta: Int,
    val valorTotalRenta: Double,
    val fechaRenta : LocalDateTime,
    val fechaEstimadaEntrega : LocalDateTime,
    val fechaEntregado : LocalDateTime





)







