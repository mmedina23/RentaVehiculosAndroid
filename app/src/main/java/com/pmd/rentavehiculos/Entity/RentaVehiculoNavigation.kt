package com.pmd.rentavehiculos.Entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class RentaVehiculoNavigation(
    val personaId : Int,
    val vehiculoId : Int,
    val token : String,
    val precioDiaVehiculo : Double,
    val Persona : PersonaRequestRenta,
    val Vehiculo : VehiculoRequestRenta
) : Parcelable