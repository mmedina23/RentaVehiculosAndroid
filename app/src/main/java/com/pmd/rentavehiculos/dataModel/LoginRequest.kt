package com.pmd.rentavehiculos.dataModel

data class LoginRequest(
    val nombre_usuario: String,  // Cambiar si el backend usa otro nombre
    val contrasena: String       // Asegúrate de que coincida
)


