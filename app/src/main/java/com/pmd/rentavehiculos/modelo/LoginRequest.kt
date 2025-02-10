package com.pmd.rentavehiculos.modelo

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    //enviar las credenciales al backend
    @SerializedName("nombre_usuario") val nombreUsuario: String,
    @SerializedName("contrasena") val contrasena: String
)
