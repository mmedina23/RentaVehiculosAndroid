package com.pmd.rentavehiculos.models


data class RentarVehiculoRequest(
    val vehiculoId: Int,   // 🔹 Asegúrate de que el campo es correcto
    val dias: Int          // 🔹 Debe ser un número entero válido
)
