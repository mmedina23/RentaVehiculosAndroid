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

    private val _rentas = MutableLiveData<List<Renta>>() // Variable mutable
    val rentas: LiveData<List<Renta>> get() = _rentas // Variable solo de lectura

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

    // Cargar todos los vehículos rentados con sus rentas asociadas
    // Cargar los vehículos rentados con sus rentas asociadas
    fun loadVehiculosRentados() = viewModelScope.launch {
        obtenerToken()?.let { token ->
            val response = vehiculoService.obtenerVehiculos(token, "disponibles") // Obtener vehículos disponibles
            if (response.isSuccessful) {
                val vehiculos = response.body() ?: emptyList()
                val rentas = mutableListOf<Renta>()
                for (vehiculo in vehiculos) {
                    val rentaResponse = rentaService.obtenerHistorialRentas(token, vehiculo.id)
                    if (rentaResponse.isSuccessful) {
                        val rentasHistorial = rentaResponse.body() ?: emptyList()
                        if (rentasHistorial.isNotEmpty()) {
                            rentas.addAll(rentasHistorial) // Guardamos las rentas
                        }
                    } else {
                        println("Error al cargar historial de rentas para el vehículo ${vehiculo.id}: ${rentaResponse.errorBody()?.string()}")
                    }
                }
                _rentas.postValue(rentas) // Almacenamos las rentas en _rentas
            } else {
                println("Error al obtener vehículos disponibles: ${response.errorBody()?.string()}")
            }
        }
    }

}
