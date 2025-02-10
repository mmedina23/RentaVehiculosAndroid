package com.pmd.rentavehiculos.Entity

import com.google.gson.annotations.SerializedName

data class Vehiculo(
    @SerializedName("disponible")
    val disponible : Boolean,

    @SerializedName("id")
    val id : Int,

    @SerializedName("plazas")
    val plazas : Int,

    @SerializedName("cambios")
    val cambios : String,

    @SerializedName("carroceria")
    val carroceria : String,

    @SerializedName("color")
    val color : String,

    @SerializedName("marca")
    val marca : String,

    @SerializedName("tipo_combustible")
    val tipoCombustible : String,

    @SerializedName("imagen")
    val imagen : String
)
