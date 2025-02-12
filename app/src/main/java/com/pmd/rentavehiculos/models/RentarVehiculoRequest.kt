package com.pmd.rentavehiculos.models

import com.google.gson.annotations.SerializedName




data class RentarVehiculoRequest(
    @SerializedName("persona") val persona: Persona,
    @SerializedName("vehiculo") val vehiculo: Vehiculo,
    @SerializedName("dias_renta") val diasRenta: Int,
    @SerializedName("valor_total_renta") val valorTotalRenta: Double,
    @SerializedName("fecha_renta") val fechaRenta: String,
    @SerializedName("fecha_estimada_entrega") val fechaEstimadaEntrega: String
)


// 🔹 Modelo simplificado de Persona para cumplir con la API
data class PersonaRequest(
    @SerializedName("nombre") val nombre: String,
    @SerializedName("apellidos") val apellidos: String,
    @SerializedName("direccion") val direccion: String,
    @SerializedName("telefono") val telefono: String,
    @SerializedName("tipo_identificacion") val tipoIdentificacion: String,
    @SerializedName("identificacion") val identificacion: String
)

// 🔹 Modelo simplificado de Vehiculo para cumplir con la API
data class VehiculoRequest(
    @SerializedName("marca") val marca: String,
    @SerializedName("color") val color: String,
    @SerializedName("carroceria") val carroceria: String,
    @SerializedName("plazas") val plazas: Int,
    @SerializedName("cambios") val cambios: String,
    @SerializedName("tipo_combustible") val tipoCombustible: String,
    @SerializedName("valor_dia") val valorDia: Double,
    @SerializedName("disponible") val disponible: Boolean,
    @SerializedName("imagen") val imagen: String
)
