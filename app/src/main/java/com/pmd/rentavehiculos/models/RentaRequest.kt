package com.pmd.rentavehiculos.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class RentaRequest(
    @SerializedName("persona") val persona: Persona,
    @SerializedName("vehiculo") val vehiculo: Vehiculo,
    @SerializedName("dias_renta") val diasRenta: Int,
    @SerializedName("valor_total_renta") val valorTotalRenta: Double,
    @SerializedName("fecha_renta") val fechaRenta: String,
    @SerializedName("fecha_estimada_entrega") val fechaEstimadaEntrega: String
)
