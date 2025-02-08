package com.pmd.rentavehiculos.modelo

data class RentaVehiculo(
    //representa un registro de renta de un vehículo
    val id: Int,
    val vehiculo: Vehiculo,        // Datos básicos del vehículo
    val persona: Persona?,         // Datos del cliente que rentó el vehículo
    val dias: Int,                 // Cantidad de días rentados
    val fechaRenta: String,        // Fecha en la que se rentó el vehículo
    val fechaPrevistaEntrega: String, // Fecha prevista de entrega
    val fechaEntrega: String?,     // Fecha en la que se entregó (null si aún no se ha entregado)
    val valorTotal: Double         // Valor total calculado de la renta
)

