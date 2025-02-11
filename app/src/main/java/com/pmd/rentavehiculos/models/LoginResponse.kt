package com.pmd.rentavehiculos.models



import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("persona") val persona: Persona,
    @SerializedName("perfil") val perfil: String,
    @SerializedName("llave") val llave: String,
    @SerializedName("fecha_exp_llave") val fechaExpLlave: String
)
