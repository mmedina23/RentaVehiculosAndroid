package com.pmd.rentavehiculos.remote

import com.pmd.rentavehiculos.model.Vehiculo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("vehiculos/disponibles")
    fun obtenerVehiculosDisponibles(): Call<List<Vehiculo>>

    @GET("vehiculos/{id}")
    fun obtenerDetalleVehiculo(@Path("id") id: Int): Call<Vehiculo>
}
