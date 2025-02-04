package com.pmd.rentavehiculos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmd.rentavehiculos.network.RetrofitClient
import com.pmd.rentavehiculos.network.Vehiculo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VehiculoViewModel : ViewModel() {
    private val _listaVehiculos = MutableStateFlow<List<Vehiculo>>(emptyList())
    val listaVehiculos: StateFlow<List<Vehiculo>> = _listaVehiculos

    init {
        obtenerListaVehiculos()
    }

    // Llamada a la API para obtener la lista de vehículos
    private fun obtenerListaVehiculos() {
        viewModelScope.launch {
            try {
                // Llama al cliente Retrofit
                val apiKey = "x-llave-api" // Reemplaza con tu clave real
                val vehiculos = RetrofitClient.apiService.obtenerVehiculos(apiKey, estado = null)
                _listaVehiculos.value = vehiculos // Actualiza el StateFlow
            } catch (e: Exception) {
                println("Error al obtener la lista de vehículos: ${e.message}")
            }
        }
    }
}

