package com.pmd.rentavehiculos.ui.theme.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.pmd.rentavehiculos.data.model.LogoutRequest
import com.pmd.rentavehiculos.data.model.Renta
import com.pmd.rentavehiculos.data.model.Vehiculo
import com.pmd.rentavehiculos.data.repository.RentaRepository
import com.pmd.rentavehiculos.data.repository.RetrofitClient
import com.pmd.rentavehiculos.data.repository.RetrofitClient.authService
import com.pmd.rentavehiculos.data.repository.SessionManager
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response

class AdminViewModel(context: Context) : ViewModel() {
    private val vehiculoService = RetrofitClient.vehiculoService
    private val repository = RentaRepository()
    private val sessionManager = SessionManager(context)

    private val _vehiculosDisponibles = MutableLiveData<List<Vehiculo>>()
    val vehiculosDisponibles: LiveData<List<Vehiculo>> get() = _vehiculosDisponibles

    val rentasLiveData = MutableLiveData<List<Renta>>()
    val errorLiveData = MutableLiveData<String>()
    val vehiculosRentadosAdminLiveData = MutableLiveData<List<VehiculoConRenta>>()

    fun obtenerToken(): String? = sessionManager.token

    // 🔹 Cargar los vehículos disponibles
    fun loadVehiculosDisponibles() = viewModelScope.launch {
        obtenerToken()?.let { token ->
            try {
                val response = vehiculoService.obtenerVehiculos(token, "disponibles")
                if (response.isSuccessful) {
                    _vehiculosDisponibles.postValue(response.body() ?: emptyList())
                } else {
                    Log.e("AdminViewModel", "❌ Error obteniendo vehículos disponibles: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("AdminViewModel", "⚠️ Excepción obteniendo vehículos disponibles", e)
                errorLiveData.postValue("Error obteniendo vehículos disponibles: ${e.message}")
            }
        }
    }

    data class VehiculoConRenta(
        val vehiculo: Vehiculo,
        val renta: Renta?
    )

    fun loadVehiculosRentadosAdmin() {
        viewModelScope.launch {
            val token = obtenerToken()
            if (token != null) {
                try {
                    val vehiculos = repository.obtenerVehiculosRentadosAdmin(token)

                    // 🚀 Obtener rentas asociadas a cada vehículo
                    val rentasPorVehiculo = vehiculos.map { vehiculo ->
                        async {
                            val rentas = repository.obtenerHistorialRentas(token, vehiculo.id)
                            val rentaMasReciente = rentas.maxByOrNull { it.fechaRenta }
                            VehiculoConRenta(vehiculo, rentaMasReciente)
                        }
                    }.awaitAll()

                    // 🔥 🔹 Incluir vehículos que están disponibles = false aunque no tengan renta asociada
                    val vehiculosSinRenta = vehiculos.filter { !it.disponible && rentasPorVehiculo.none { it.vehiculo.id == it.vehiculo.id } }
                        .map { VehiculoConRenta(it, null) }

                    // 🔄 Combinar ambas listas (con y sin rentas)
                    val vehiculosFinal = rentasPorVehiculo + vehiculosSinRenta

                    vehiculosRentadosAdminLiveData.postValue(vehiculosFinal)

                } catch (ex: Exception) {
                    Log.e("AdminViewModel", "❌ Error obteniendo vehículos rentados", ex)
                    errorLiveData.postValue("Error al obtener vehículos rentados: ${ex.message}")
                }
            } else {
                Log.e("AdminViewModel", "❌ Error: API Key no disponible en SessionManager")
                errorLiveData.postValue("Error: Sesión no iniciada.")
            }
        }
    }





    fun obtenerHistorialRentas(apiKey: String, vehiculoId: Int) {
        viewModelScope.launch {
            try {
                // Asegúrate de haber implementado esta función en el repositorio
                val historial = repository.obtenerHistorialRentas(apiKey, vehiculoId)
                rentasLiveData.value = historial
            } catch (ex: Exception) {
                errorLiveData.value = "Error al obtener historial de rentas: ${ex.message}"
            }
        }
    }

    fun logout(onLogoutSuccess: () -> Unit, onLogoutError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val logoutRequest = LogoutRequest(
                    id_usuario = sessionManager.personaId ?: 0,
                    llave_api = sessionManager.token ?: ""
                )

                val response: Response<Void> = authService.logout(logoutRequest)

                if (response.isSuccessful) {
                    onLogoutSuccess() // 🔹 Primero navegar fuera de la pantalla

                    delay(500) // 🔹 Pequeña pausa para evitar que la UI se actualice antes de salir

                    sessionManager.clearSession() // Ahora limpiar la sesión
                    _vehiculosDisponibles.value = emptyList() // Ahora limpiar lista de vehículos

                } else {
                    onLogoutError("Error al cerrar sesión: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                onLogoutError("Excepción: ${e.localizedMessage}")
            }
        }
    }



}
