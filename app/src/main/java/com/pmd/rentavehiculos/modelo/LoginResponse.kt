package com.pmd.rentavehiculos.modelo

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    // respuesta del login incluirá el token, el perfil y la persona
    val persona: Persona,
    val perfil: String,
    @SerializedName("llave")
    val token: String,
    @SerializedName("fecha_exp_llave")
    val fechaExpLlave: String
)

