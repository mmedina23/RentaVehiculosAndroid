package com.pmd.rentavehiculos.models

data class RentaRequest(
    val usuarioId: Int,
    val vehiculoId: Int,
    val dias: Int
)
