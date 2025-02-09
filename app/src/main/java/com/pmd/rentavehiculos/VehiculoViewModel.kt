package com.pmd.rentavehiculos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmd.rentavehiculos.data.RetrofitServiceFactory
import com.pmd.rentavehiculos.data.model.Vehiculo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VehiculoViewModel : ViewModel() {

    private val _vehiculos = MutableStateFlow<List<Vehiculo>>(emptyList())
    val vehiculos: StateFlow<List<Vehiculo>> = _vehiculos

    private val apiService = RetrofitServiceFactory.retrofitService()

    init {
        obtenerVehiculos()
    }

    private fun obtenerVehiculos() {
        viewModelScope.launch {
            try {
                val respuesta = apiService.listaVehiculosDisponibles("08022025201704807", "disponibles")
                _vehiculos.value = respuesta
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
