package com.pmd.rentavehiculos.Entity

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Logout(
    @SerializedName("id_usuario")
    val idUsuario : Int,
    @SerializedName("llave_api")
    val token : String
)
