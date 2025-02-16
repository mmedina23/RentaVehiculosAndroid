package com.pmd.rentavehiculos.network

import com.pmd.rentavehiculos.model.Vehiculo

// Modelo de la solicitud de login
data class LoginRequest(
    val nombre_usuario: String,
    val contrasena: String
)

data class LogoutRequest(
    val id_usuario: Int,
    val llave_api: String
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

data class Renta(
    val id: Int,
    val persona: Persona,
    val vehiculo: Vehiculo,
    val diasRenta: Int,
    val valorTotalRenta: Double,
    val fechaRenta: String,
    val fechaEstimadaEntrega: String,
    val fechaEntregado: String? // Puede ser null si el vehículo no ha sido entregado
)
