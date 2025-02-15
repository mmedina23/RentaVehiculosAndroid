package com.pmd.rentavehiculos.ui.theme.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmd.rentavehiculos.data.model.PersonaRequest
import com.pmd.rentavehiculos.data.model.Renta
import com.pmd.rentavehiculos.data.model.RentarVehiculoRequest
import com.pmd.rentavehiculos.data.model.Vehiculo
import com.pmd.rentavehiculos.data.repository.RentaService
import com.pmd.rentavehiculos.data.repository.SessionManager
import com.pmd.rentavehiculos.data.repository.VehiculoService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ClienteViewModel(private val rentaService: RentaService, private val vehiculoService: VehiculoService, private val sessionManager: SessionManager) : ViewModel() {

    private val _vehiculosDisponibles = MutableStateFlow<List<Vehiculo>>(emptyList())
    val vehiculosDisponibles: StateFlow<List<Vehiculo>> = _vehiculosDisponibles

    private val _vehiculosRentados = MutableStateFlow<List<Renta>>(emptyList())
    val vehiculosRentados: StateFlow<List<Renta>> = _vehiculosRentados

    init {
        cargarVehiculosDisponibles()
        cargarVehiculosRentados()
    }

    private fun cargarVehiculosDisponibles() {
        viewModelScope.launch {
            try {
                val response = vehiculoService.obtenerVehiculos(sessionManager.token!!)
                if (response.isSuccessful) {
                    _vehiculosDisponibles.value = response.body() ?: emptyList()
                } else {
                    Log.e("ClienteViewModel", "Error obteniendo vehículos disponibles: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("ClienteViewModel", "Excepción obteniendo vehículos disponibles", e)
            }
        }
    }

    private fun cargarVehiculosRentados() {
        viewModelScope.launch {
            sessionManager.personaId?.let { personaId ->
                try {
                    val response = rentaService.obtenerVehiculosRentados(sessionManager.token!!, personaId)
                    if (response.isSuccessful) {
                        _vehiculosRentados.value = response.body() ?: emptyList()
                    } else {
                        Log.e("ClienteViewModel", "Error obteniendo vehículos rentados: ${response.message()}")
                    }
                } catch (e: Exception) {
                    Log.e("ClienteViewModel", "Excepción obteniendo vehículos rentados", e)
                }
            }
        }
    }

    fun rentarVehiculo(vehiculo: Vehiculo) {
        if (_vehiculosRentados.value.size >= 3) {
            Log.e("ClienteViewModel", "No puedes rentar más de 3 vehículos.")
            return
        }

        viewModelScope.launch {
            try {
                val personaRequest = PersonaRequest(sessionManager.personaId.toString())
                val diasRenta = 5 // Valor arbitrario, se puede mejorar con selección del usuario
                val valorTotalRenta = diasRenta * vehiculo.valorDia
                val rentaRequest = RentarVehiculoRequest(
                    persona = personaRequest,
                    dias_renta = diasRenta,
                    valor_total_renta = valorTotalRenta,
                    fecha_renta = "2025-02-15", // Fecha dummy
                    fecha_estimada_entrega = "2025-02-20" // Fecha dummy
                )
                val response = rentaService.reservarVehiculo(sessionManager.token!!, vehiculo.id, rentaRequest)
                if (response.isSuccessful) {
                    cargarVehiculosDisponibles()
                    cargarVehiculosRentados()
                } else {
                    Log.e("ClienteViewModel", "Error al rentar vehículo: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("ClienteViewModel", "Excepción al rentar vehículo", e)
            }
        }
    }

    fun entregarVehiculo(renta: Renta) {
        viewModelScope.launch {
            try {
                val response = rentaService.entregarVehiculo(sessionManager.token!!, renta.vehiculo.id)
                if (response.isSuccessful) {
                    cargarVehiculosDisponibles()
                    cargarVehiculosRentados()
                } else {
                    Log.e("ClienteViewModel", "Error al entregar vehículo: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("ClienteViewModel", "Excepción al entregar vehículo", e)
            }
        }
    }
}
