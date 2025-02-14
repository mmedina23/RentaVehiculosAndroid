// ViewModel: AdminViewModel.kt
package com.pmd.rentavehiculos.ui.theme.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.pmd.rentavehiculos.data.model.Renta
import com.pmd.rentavehiculos.data.model.Vehiculo
import com.pmd.rentavehiculos.data.repository.RetrofitClient
import com.pmd.rentavehiculos.data.repository.RetrofitClient.rentaService
import com.pmd.rentavehiculos.data.repository.SessionManager
import kotlinx.coroutines.launch
class AdminViewModel(context: Context) : ViewModel() {
    private val vehiculoService = RetrofitClient.vehiculoService
    private val sessionManager = SessionManager(context)

    private val _vehiculosDisponibles = MutableLiveData<List<Vehiculo>>()
    val vehiculosDisponibles: LiveData<List<Vehiculo>> get() = _vehiculosDisponibles

    private val _vehiculosRentados = MutableLiveData<List<Vehiculo>>() // Aquí almacenamos los vehículos rentados
    val vehiculosRentados : LiveData<List<Vehiculo>> get() = _vehiculosRentados

    private fun obtenerToken(): String? = sessionManager.token

    // Cargar los vehículos disponibles
    fun loadVehiculosDisponibles() = viewModelScope.launch {
        obtenerToken()?.let { token ->
            val response = vehiculoService.obtenerVehiculos(token, "disponibles")
            if (response.isSuccessful) {
                _vehiculosDisponibles.postValue(response.body() ?: emptyList())
            }
        }
    }

    // Cargar historial de rentas para un vehículo específico
    fun loadVehiculosRentadosConHistorial(vehiculoId: Int) = viewModelScope.launch {
        obtenerToken()?.let { token ->
            val response = rentaService.obtenerHistorialRentas(token, vehiculoId)
            if (response.isSuccessful) {
                _vehiculosRentados.postValue((response.body() ?: emptyList()) as List<Vehiculo>?) // Aquí asignamos a vehiculosRentados
            } else {
                println("Error al cargar historial de rentas: ${response.errorBody()?.string()}")
            }
        }
    }

    // Cargar todos los vehículos rentados
    fun loadVehiculosRentados() = viewModelScope.launch {
        obtenerToken()?.let { token ->
            val response = vehiculoService.obtenerVehiculos(token, "disponibles") // Obtener vehículos disponibles
            if (response.isSuccessful) {
                val vehiculos = response.body() ?: emptyList()
                // Filtrar los vehículos rentados
                val vehiculosRentados = mutableListOf<Vehiculo>()
                for (vehiculo in vehiculos) {
                    val rentaResponse = rentaService.obtenerHistorialRentas(token, vehiculo.id)
                    if (rentaResponse.isSuccessful) {
                        val rentas = rentaResponse.body() ?: emptyList()
                        if (rentas.isNotEmpty()) { // Si tiene rentas, significa que está rentado
                            vehiculosRentados.add(vehiculo)
                        }
                    } else {
                        println("Error al cargar historial de rentas para el vehículo ${vehiculo.id}: ${rentaResponse.errorBody()?.string()}")
                    }
                }
                _vehiculosRentados.postValue(vehiculosRentados) // Aquí almacenamos los vehículos rentados
            } else {
                println("Error al obtener vehículos disponibles: ${response.errorBody()?.string()}")
            }
        }
    }
}
