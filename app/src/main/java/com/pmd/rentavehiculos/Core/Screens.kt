package com.pmd.rentavehiculos.Core

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
object SplashScreen

@Serializable
data class Persona(val token: String)

@Serializable
object Login

@Serializable
data class Cliente(val token: String)

@Serializable
data class Admind(val token: String)