package com.pmd.rentavehiculos.models

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("nombre_usuario") val nombreUsuario: String,
    @SerializedName("contrasena") val contrasena: String
)
