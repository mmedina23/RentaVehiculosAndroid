package com.pmd.rentavehiculos.Entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class PersonaRequestRenta(
    @SerializedName("id")
    val personaId : Int,
    @SerializedName("nombre")
    val nombre : String,
    @SerializedName("apellidos")
    val apellido : String,
    @SerializedName("direccion")
    val direccion : String,
    @SerializedName("telefono")
    val telefono : String,
    @SerializedName("tipo_identificacion")
    val tipoIdentificacion : String,
    @SerializedName("identificacion")
    val identificacion : String
) : Parcelable