package com.pmd.rentavehiculos.data.model;

data class Vehiculo (

    val cambios : String,
    val carroceria : String,
    val color : String,
    val disponible : Boolean,
    val id : Int,
    val marca : String,
    val plazas : Int,
    val tipoCombustible : String,
    val valorDia : Int,
    
)
