package com.pmd.rentavehiculos.model


data class CarRental(
    val id: Int,
    val cliente: String,
    val vehiculo: String,
    val fechaInicio: String,
    val fechaFin: String,
    val totalPago: Double
)
