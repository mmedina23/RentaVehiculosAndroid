package com.pmd.rentavehiculos.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmd.rentavehiculos.modelo.RentaVehiculo
import com.pmd.rentavehiculos.modelo.RentarVehiculoResponse
import com.pmd.rentavehiculos.repositorio.RentaRepository
import kotlinx.coroutines.launch

class VehiculosRentadosViewModel : ViewModel() {

    private val repository = RentaRepository()

    val vehiculosRentadosLiveData = MutableLiveData<List<RentaVehiculo>>()
    val errorLiveData = MutableLiveData<String>()

    fun obtenerVehiculosRentados(apiKey: String, personaId: Int) {
        viewModelScope.launch {
            try {
                val rentas: List<RentaVehiculo> = repository.obtenerVehiculosRentados(apiKey, personaId)
                vehiculosRentadosLiveData.value = rentas
            } catch (ex: Exception) {
                errorLiveData.value = "Error al obtener vehículos rentados: ${ex.message}"
            }
        }
    }


    fun entregarVehiculo(apiKey: String, vehiculoId: Int) {
        viewModelScope.launch {
            try {
                repository.entregarVehiculo(apiKey, vehiculoId)
            } catch (ex: Exception) {
                errorLiveData.value = "Error al entregar vehículo: ${ex.message}"
            }
        }
    }
}