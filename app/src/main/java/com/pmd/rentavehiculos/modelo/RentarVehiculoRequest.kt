package com.pmd.rentavehiculos.modelo

data class RentarVehiculoRequest(
    //se envía este request con el identificador del vehículo y la cantidad de días que desea
    val vehiculoId: Int,
    val dias: Int
)
