package com.pmd.rentavehiculos.Entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class PersonaRequestRenta(
    val nombre : String,
    val apellido : String,
    val direccion : String,
    val telefono : String,
    val tipoIdentificacion : String,
    val identificacion : String
) : Parcelable