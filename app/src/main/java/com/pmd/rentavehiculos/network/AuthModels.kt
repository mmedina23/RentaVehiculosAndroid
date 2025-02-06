package com.pmd.rentavehiculos.network

// Modelo de la solicitud de login
data class LoginRequest(
    val nombre_usuario: String,
    val contrasena: String
)


// Modelo de la respuesta del login
data class LoginResponse(
    val persona: Persona,
    val perfil: String,
    val llave: String,
    val fechaExpLlave: String
)

// Información básica del usuario
data class Persona(
    val id: Int,
    val nombre: String,
    val apellidos: String,
    val direccion: String,
    val telefono: String,
    val tipoIdentificacion: String,
    val identificacion: String
)
