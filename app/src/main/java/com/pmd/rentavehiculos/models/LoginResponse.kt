package com.pmd.rentavehiculos.models

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val token: String,
    @SerializedName("role") val role: String // Asegúrate de que el campo sea correcto
)
