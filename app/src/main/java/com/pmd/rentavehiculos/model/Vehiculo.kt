package com.pmd.rentavehiculos.model


data class Vehiculo(
    val id: Int,
    val marca: String,
    val color: String,
    val carroceria: String,
    val plazas: Int,
    val cambios: String,
    val tipo_combustible: String,
    val valor_dia: Int,
    val disponible: Boolean, // 🔥 Asegúrate de que esta línea esté aquí
    val imagen: String
)
