package com.pmd.rentavehiculos.Entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class VehiculoRequestRenta(
    val marca : String,
    val color : String,
    val carroceria : String,
    val plazas : Int,
    val cambios : String,
    val tipoCombustible : String,
    val valorDia : Double,
    val disponible : Boolean,
    val imagen : String
) : Parcelable