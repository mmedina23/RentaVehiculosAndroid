package com.pmd.rentavehiculos.Entity

import com.google.gson.annotations.SerializedName

data class UserRequest(
    @SerializedName("nombre_usuario")
    val userName : String,
    @SerializedName("contrasena")
    val password : String
)