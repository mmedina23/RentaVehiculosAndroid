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

    fun obtenerVehiculos(llaveApi: String) {
        viewModelScope.launch {
            try {
                val vehiculos = RetrofitClient.vehiculoApi.obtenerVehiculos(llaveApi)
                _listaVehiculos.value = vehiculos
            } catch (e: Exception) {
                println("Error al obtener vehículos: ${e.message}")
            }
        }
    }
}
