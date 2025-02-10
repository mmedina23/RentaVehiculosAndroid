package com.pmd.rentavehiculos.viewModel

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
    private val _vehiculoPorId = MutableStateFlow<Vehiculo?>(null)
    val vehiculoPorId: StateFlow<Vehiculo?> = _vehiculoPorId
    private val apiService = RetrofitServiceFactory.vehiculoService()

    init {
        obtenerVehiculos()
        obtenerVehiculosPorId(20)

    }

    private fun obtenerVehiculos() {
        viewModelScope.launch {
            try {
                val respuesta = apiService.listaVehiculosDisponibles("10022025184637426", "disponibles")
                _vehiculos.value = respuesta
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun obtenerVehiculosPorId(id: Int) {
        viewModelScope.launch {
            try {
                val respuesta = apiService.listaVehiculosPorId("10022025184637426", id)
                _vehiculoPorId.value = respuesta
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun ingresoVehiculo(vehiculo: Vehiculo) {
        viewModelScope.launch {
            try {
                val vehiculoInsertado = apiService.insertarVehiculo("10022025184637426", vehiculo)
                _vehiculos.value = _vehiculos.value + vehiculoInsertado
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    fun actualizarVehiculoPorId(id: Int, vehiculoActualizado: Vehiculo) {
        viewModelScope.launch {
            try {
                val respuesta = apiService.actualizarVehiculoPorId("10022025184637426", id , vehiculoActualizado)
                _vehiculos.value = _vehiculos.value.map { if (it.id == id) respuesta else it } as List<Vehiculo>
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    fun eliminarVehiculoPorId(id: Int) {
        viewModelScope.launch {
            try {
                // Llamada a la API para eliminar el vehículo
                val respuesta = apiService.eliminarVehiculosPorId("10022025184637426", id)

                // Actualizamos la lista de vehículos con la respuesta
                _vehiculos.value = _vehiculos.value.filter { it.id != respuesta.id }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }





}
