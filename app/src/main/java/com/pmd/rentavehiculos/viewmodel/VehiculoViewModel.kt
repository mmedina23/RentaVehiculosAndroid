package com.pmd.rentavehiculos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmd.rentavehiculos.model.Vehiculo
import com.pmd.rentavehiculos.network.Renta
import com.pmd.rentavehiculos.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VehiculoViewModel : ViewModel() {
    private val _listaVehiculosDisponibles = MutableStateFlow<List<Vehiculo>>(emptyList())
    val listaVehiculosDisponibles: StateFlow<List<Vehiculo>> = _listaVehiculosDisponibles

    private val _listaVehiculosRentados = MutableStateFlow<List<Renta>>(emptyList())
    val listaVehiculosRentados: StateFlow<List<Renta>> = _listaVehiculosRentados

    fun obtenerVehiculosDisponibles(llaveApi: String) {
        viewModelScope.launch {
            try {
                val vehiculos = RetrofitClient.vehiculoApi.obtenerVehiculos(llaveApi)
                _listaVehiculosDisponibles.value = vehiculos
            } catch (e: Exception) {
                println("Error obteniendo vehículos disponibles: ${e.message}")
            }
        }
    }

    fun obtenerVehiculosRentados(idUsuario: Int, llaveApi: String) {
        viewModelScope.launch {
            try {
                val vehiculosRentados =
                    RetrofitClient.vehiculoApi.obtenerVehiculosRentados(idUsuario, llaveApi)
                _listaVehiculosRentados.value = vehiculosRentados
            } catch (e: Exception) {
                println("Error obteniendo vehículos rentados: ${e.message}")
            }
        }
    }

    fun reservarVehiculo(
        idVehiculo: Int,
        idUsuario: Int,
        llaveApi: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.vehiculoApi.reservarVehiculo(idVehiculo, llaveApi)

                println("🔍 Respuesta del servidor: Código ${response.code()} - ${response.message()}")

                if (response.isSuccessful) {
                    onSuccess("Vehículo rentado exitosamente.")
                    obtenerVehiculosDisponibles(llaveApi) // Refrescar lista
                    obtenerVehiculosRentados(idUsuario, llaveApi) // Actualizar lista de rentados
                } else {
                    onError("Error al rentar vehículo. Código: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                onError("Error en la conexión: ${e.message}")
            }
        }
    }
}

