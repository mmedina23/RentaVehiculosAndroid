package com.pmd.rentavehiculos.modelo

data class RentarVehiculoResponse(
    val success: Boolean,     // Indica si la reserva se realizó correctamente
    val message: String,      // Mensaje descriptivo (por ejemplo, "Reserva exitosa" o "Error: vehículo no disponible")
    val rentalId: Int?,       // Identificador de la reserva (si aplica)
    val totalPrice: Double?   // Valor total de la reserva (opcional, según lo que devuelva tu API)
)