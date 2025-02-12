package com.pmd.rentavehiculos.repositorio


import android.util.Log
import com.pmd.rentavehiculos.modelo.LoginResponse
import com.pmd.rentavehiculos.modelo.LoginRequest
import com.pmd.rentavehiculos.modelo.Vehiculo
import com.pmd.rentavehiculos.modelo.RentarVehiculoRequest
import com.pmd.rentavehiculos.modelo.RentarVehiculoResponse
import com.pmd.rentavehiculos.data.RetrofitClient
import com.pmd.rentavehiculos.modelo.RentaVehiculo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
class RentaRepository {

        // Método para hacer login
        suspend fun login(loginRequest: LoginRequest): LoginResponse {
            val response: Response<LoginResponse> = RetrofitClient.authService.login(loginRequest)
            if (response.isSuccessful) {
                return response.body() ?: throw Exception("La respuesta de login es nula")
            } else {
                throw HttpException(response)
            }
        }

        // Obtener vehículos disponibles
        suspend fun obtenerVehiculosDisponibles(apiKey: String): List<Vehiculo> {
            val response: Response<List<Vehiculo>> =
                RetrofitClient.vehiculosService.obtenerVehiculos(apiKey)
            if (response.isSuccessful) {
                return response.body() ?: emptyList()
            } else {
                throw HttpException(response)
            }
        }

        // Reservar un vehículo
        suspend fun reservarVehiculo(
            apiKey: String,
            vehiculoId: Int,
            rentaRequest: RentarVehiculoRequest
        ): Unit {
            val response: Response<Unit> =
                RetrofitClient.vehiculosService.reservarVehiculo(apiKey, vehiculoId, rentaRequest)
            if (response.isSuccessful) {
                return
            } else {
                val errorBody = response.errorBody()?.string()
                throw HttpException(response)
            }
        }

        // Entregar un vehículo
        suspend fun entregarVehiculo(apiKey: String, vehiculoId: Int) {
            val response: Response<Unit> =
                RetrofitClient.vehiculosService.entregarVehiculo(apiKey, vehiculoId)
            if (!response.isSuccessful) {
                throw HttpException(response)
            }
        }
        // Obtener la lista de vehículos rentados por una persona
        suspend fun obtenerVehiculosRentados(apiKey: String, personaId: Int): List<RentaVehiculo> {
            val response = RetrofitClient.vehiculosService.obtenerVehiculosRentados(apiKey, personaId)
            if (response.isSuccessful) {
                return response.body() ?: emptyList()
            } else {
                throw retrofit2.HttpException(response)
            }
        }

        // Actualizar un vehículo
        suspend fun actualizarVehiculo(
            apiKey: String,
            vehiculoId: Int,
            vehiculo: Vehiculo
        ): Vehiculo {
            val response: Response<Vehiculo> =
                RetrofitClient.vehiculosService.actualizarVehiculo(apiKey, vehiculoId, vehiculo)
            if (response.isSuccessful) {
                return response.body() ?: throw Exception("La respuesta de actualización es nula")
            } else {
                throw HttpException(response)
            }
        }

        // Eliminar un vehículo
        suspend fun eliminarVehiculo(apiKey: String, vehiculoId: Int) {
            val response: Response<Unit> =
                RetrofitClient.vehiculosService.eliminarVehiculo(apiKey, vehiculoId)
            if (!response.isSuccessful) {
                throw HttpException(response)
            }
        }

        // Crear un nuevo vehículo
        suspend fun crearVehiculo(apiKey: String, vehiculo: Vehiculo): Vehiculo {
            val response: Response<Vehiculo> =
                RetrofitClient.vehiculosService.crearVehiculo(apiKey, vehiculo)
            if (response.isSuccessful) {
                return response.body() ?: throw Exception("La respuesta de creación es nula")
            } else {
                throw HttpException(response)
            }
        }

        // Obtener el detalle de un vehículo
        suspend fun obtenerDetalleVehiculo(apiKey: String, vehiculoId: Int): Vehiculo {
            val response: Response<Vehiculo> =
                RetrofitClient.vehiculosService.obtenerDetalleVehiculo(apiKey, vehiculoId)
            if (response.isSuccessful) {
                return response.body() ?: throw Exception("La respuesta de detalle del vehículo es nula")
            } else {
                val errorBody = response.errorBody()?.string()
                throw HttpException(response)
            }
        }

        // Obtener el historial de rentas global para el admin
        suspend fun obtenerTodasLasRentas(apiKey: String): List<RentaVehiculo> {
            val response = RetrofitClient.vehiculosService.obtenerTodasLasRentas(apiKey)
            if (response.isSuccessful) {
                return response.body() ?: emptyList()
            } else {
                throw HttpException(response)
            }
        }

}