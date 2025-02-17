package com.pmd.rentavehiculos.Entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
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

    @SerializedName("valor_dia")
    val precioDia : Int,

    @SerializedName("tipo_combustible")
    val tipoCombustible : String,

    @SerializedName("imagen")
    val imagen : String
) : Parcelable