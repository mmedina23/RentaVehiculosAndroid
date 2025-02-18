package com.pmd.rentavehiculos.Entity

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class RentaRequest (
    @SerializedName("persona")
    val persona : PersonaRequestRenta,
    @SerializedName("dias_renta")
    val diasRenta : Int,
    @SerializedName("valor_total_renta")
    val valorTotalRenta : Int,
    @SerializedName("fecha_renta")
    val fechaRenta : String,
    @SerializedName("fecha_estimada_entrega")
    val fechaEstimadaEntrega : String
)