package com.pmd.rentavehiculos.Entity

import com.google.gson.annotations.SerializedName


data class RentaRequest (
    val persona : PersonaRequestRenta,
    val vehiculo : VehiculoRequestRenta,
    val diasRenta : Double,
    val fechaRenta : String,
    val fechaEstimadaEntrega : String
)