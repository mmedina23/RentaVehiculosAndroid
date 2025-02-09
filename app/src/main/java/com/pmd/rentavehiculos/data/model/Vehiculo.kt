package com.pmd.rentavehiculos.data.model

import com.google.gson.annotations.SerializedName

data class Vehiculo(
    @SerializedName("cambios")
    val cambios: String,

    @SerializedName("carroceria")
    val carroceria: String,

    @SerializedName("color")
    val color: String,

    @SerializedName("disponible")
    val disponible: Boolean,

    @SerializedName("id")
    val id: Int,

    @SerializedName("marca")
    val marca: String,

    @SerializedName("plazas")
    val plazas: Int,

    @SerializedName("tipo_combustible")
    val tipoCombustible: String,

    @SerializedName("valor_dia")
    val valorDia: Double
)
