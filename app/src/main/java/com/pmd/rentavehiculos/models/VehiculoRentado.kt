package com.pmd.rentavehiculos.models

import com.google.gson.annotations.SerializedName

data class VehiculoRentado(
    val id: Int,
    val marca: String,
    @SerializedName("carroceria") val carroceria: String, // Si la API no tiene "modelo"
    @SerializedName("fecha_renta") val fechaRenta: String,
    @SerializedName("fecha_entrega") val fechaEntrega: String?,
    @SerializedName("precio_total") val precioTotal: Double,
    @SerializedName("cliente_nombre") val clienteNombre: String,
    @SerializedName("cliente_apellido") val clienteApellido: String,
    @SerializedName("dias-rentados") val diasRentados: String
) {

}
