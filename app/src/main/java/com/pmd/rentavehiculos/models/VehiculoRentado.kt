package com.pmd.rentavehiculos.models

data class VehiculoRentado(
    val id: Int,
    val marca: String,
    val modelo: String,
    val fechaRenta: String,
    val fechaEntrega: String,
    val precioTotal: Double,
    val imagenUrl: String
)
