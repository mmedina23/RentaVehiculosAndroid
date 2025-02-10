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

    // Obtener vehículos disponibles (renombrado para que coincida con el ViewModel)
    suspend fun obtenerVehiculosDisponibles(apiKey: String): List<Vehiculo> {
        return RetrofitClient.vehiculosService.obtenerVehiculos(apiKey)
    }

    // Reservar un vehículo
    suspend fun reservarVehiculo(apiKey: String, vehiculoId: Int, rentaRequest: RentarVehiculoRequest): Response<RentarVehiculoRequest> {
        return RetrofitClient.vehiculosService.reservarVehiculo(apiKey, vehiculoId, rentaRequest)
    }

    // Entregar un vehículo
    suspend fun entregarVehiculo(apiKey: String, vehiculoId: Int): Response<Unit> {
        return RetrofitClient.vehiculosService.entregarVehiculo(apiKey, vehiculoId)
    }

    // Actualizar un vehículo
    suspend fun actualizarVehiculo(apiKey: String, vehiculoId: Int, vehiculo: Vehiculo): Vehiculo {
        return RetrofitClient.vehiculosService.actualizarVehiculo(apiKey, vehiculoId, vehiculo)
    }

    // Eliminar un vehículo
    suspend fun eliminarVehiculo(apiKey: String, vehiculoId: Int): Response<Unit> {
        return RetrofitClient.vehiculosService.eliminarVehiculo(apiKey, vehiculoId)
    }

    // Crear un nuevo vehículo
    suspend fun crearVehiculo(apiKey: String, vehiculo: Vehiculo): Response<Vehiculo> {
        return RetrofitClient.vehiculosService.crearVehiculo(apiKey, vehiculo)
    }
}