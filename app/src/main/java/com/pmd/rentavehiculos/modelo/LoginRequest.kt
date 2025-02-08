package com.pmd.rentavehiculos.modelo

data class LoginRequest(
    //enviar las credenciales al backend
    val usuario: String,
    val contrasena: String
)
