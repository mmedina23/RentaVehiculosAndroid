package com.pmd.rentavehiculos.Core

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.pmd.rentavehiculos.Entity.PersonaRequestRenta
import com.pmd.rentavehiculos.Entity.Usuario
import com.pmd.rentavehiculos.Entity.VehiculoRequestRenta
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
object SplashScreen

@Serializable
data class Persona(val token: String)

@Serializable
object Login

@Parcelize
@Serializable
data class Cliente(val token: String, val personaId : Int, val Persona : PersonaRequestRenta) : Parcelable

@Serializable
data class Admind(val token: String, val personaId: Int, val Persona : PersonaRequestRenta)

@Serializable
data class RentarVehiculo(val personaId: Int, val vehiculoId : Int, val token: String, val precioDiaVehiculo : Double, val Persona: PersonaRequestRenta, val Vehiculo : VehiculoRequestRenta)

@Parcelize
@Serializable
data class MisVehiculos(val token : String, val personaId : Int) : Parcelable

@Parcelize
@Serializable
data class VehiculoRentadoDetalle(val token : String) : Parcelable