package com.pmd.rentavehiculos.repositorio


import com.pmd.rentavehiculos.modelo.LoginResponse
import com.pmd.rentavehiculos.modelo.LoginRequest
import com.pmd.rentavehiculos.modelo.Vehiculo
import com.pmd.rentavehiculos.modelo.RentarVehiculoRequest
import com.pmd.rentavehiculos.data.RetrofitClient
import retrofit2.Response

class RentaRepository {

    // Método para hacer login
    suspend fun login(loginRequest: LoginRequest): LoginResponse {
        return RetrofitClient.authService.login(loginRequest)
    }

    // Obtener vehículos disponibles
    suspend fun obtenerVehiculos(apiKey: String): List<Vehiculo> {
        return RetrofitClient.vehiculosService.obtenerVehiculos(apiKey)
    }

    // Reservar un vehículo
    suspend fun reservarVehiculo(apiKey: String, vehiculoId: Int, rentaRequest: RentarVehiculoRequest): Response<RentarVehiculoRequest> {
        return RetrofitClient.vehiculosService.reservarVehiculo(apiKey, vehiculoId, rentaRequest)
    }


}