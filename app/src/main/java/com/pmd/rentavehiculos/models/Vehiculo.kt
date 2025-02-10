package com.pmd.rentavehiculos.models

import com.google.gson.annotations.SerializedName

data class Vehiculo(
    val id: Int,
    val marca: String,
    val modelo: String, // Asegúrate de que existe este campo
    val año: Int,
    val color: String,
    val carroceria: String,
    val plazas: Int,
    val cambios: String,
    @SerializedName("tipo_combustible") val tipoCombustible: String,
    @SerializedName("valor_dia") val valorDia: Double, // Asegúrate de que el tipo sea Double
    val disponible: Boolean,
    val imagen: String,


)












