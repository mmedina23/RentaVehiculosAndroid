package com.pmd.rentavehiculos.data.model

import com.google.gson.annotations.SerializedName

data class Renta(
    val id: Int,
    val persona: Persona,
    val vehiculo: Vehiculo,
    @SerializedName("dias_renta")
    val dias: Int,
    @SerializedName("valor_total_renta")
    val valorTotal: Double,
    @SerializedName("fecha_renta")
    val fechaRenta: String,
    @SerializedName("fecha_estimada_entrega")
    val fechaPrevistaEntrega: String,
    @SerializedName("fecha_entregado")
    val fechaEntrega: String?
)
