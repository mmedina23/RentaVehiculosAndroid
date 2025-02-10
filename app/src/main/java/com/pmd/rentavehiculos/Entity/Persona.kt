package com.pmd.rentavehiculos.Entity

import com.google.gson.annotations.SerializedName

data class Persona (
    @SerializedName("id")
    val id : Int,
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
)