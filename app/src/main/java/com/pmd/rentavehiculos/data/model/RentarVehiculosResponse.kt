package com.pmd.rentavehiculos.data.model

data class RentarVehiculoResponse(
    val success: Boolean,
    val message: String,
    val rentalId: Double?,
    val totalPrice: Double?
)
