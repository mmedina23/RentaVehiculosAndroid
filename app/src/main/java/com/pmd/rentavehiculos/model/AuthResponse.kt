package com.pmd.rentavehiculos.model

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    val llave: String, // API Key
    val perfil: String, // ADMIN o CLIENTE
    val persona: Persona,
    @SerializedName("fecha_exp_llave") val fechaExpLlave: String // ✅ Asegurar que el nombre coincida con la API
)
