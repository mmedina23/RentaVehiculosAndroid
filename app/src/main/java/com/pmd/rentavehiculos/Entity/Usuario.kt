package com.pmd.rentavehiculos.Entity

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


@Serializable
data class Usuario(
    @SerializedName("persona")
    val persona : Persona,
    @SerializedName("perfil")
    val perfil : String,
    @SerializedName("llave")
    val llave : String,
    @SerializedName("fecha_exp_llave")
    val fechaExpLlave : String
)