package com.pmd.rentavehiculos.data.model

import com.google.gson.annotations.SerializedName

data class RentarVehiculoRequest(
    @SerializedName("persona")
    val persona: PersonaRequest,
    @SerializedName("dias_renta")
    val dias_renta: Int,
    @SerializedName("valor_total_renta")
    val valor_total_renta: Int,
    @SerializedName("fecha_renta")
    val fecha_renta: String,
    @SerializedName("fecha_estimada_entrega")
    val fecha_estimada_entrega: String
)

data class PersonaRequest(
    @SerializedName("id")
    val id: String
)
