package com.pmd.rentavehiculos.models

data class VehiculoRentado(
    val id: Int,
    val marca: String,
    val modelo: String,
    val fechaRenta: String,      // Fecha en la que se rentó
    val fechaEntrega: String?,   // Fecha prevista de entrega
    val precioTotal: Double      // Total pagado
)
