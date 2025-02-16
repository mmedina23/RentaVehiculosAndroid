package com.pmd.rentavehiculos.data.repository

import com.pmd.rentavehiculos.data.model.Renta
import com.pmd.rentavehiculos.data.model.Vehiculo
import retrofit2.HttpException
import retrofit2.Response

class RentaRepository {


    // 🔹 Obtener historial de rentas de un vehículo
    suspend fun obtenerHistorialRentas(apiKey: String, vehiculoId: Int): List<Renta> {
        val response = RetrofitClient.vehiculoService.obtenerHistorialRentas(apiKey, vehiculoId)
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw HttpException(response)
        }
    }

    // 🔹 Obtener vehículos rentados por una persona
    suspend fun obtenerVehiculosRentadosAdmin(apiKey: String): List<Vehiculo> {
        val response: Response<List<Vehiculo>> =
            RetrofitClient.vehiculoService.obtenerVehiculosAdmin(apiKey)
        if (response.isSuccessful) {
            // Filtra los vehículos que NO están disponibles (es decir, rentados)
            return response.body()?.filter { !it.disponible } ?: emptyList()
        } else {
            throw HttpException(response)
        }
    }

}
