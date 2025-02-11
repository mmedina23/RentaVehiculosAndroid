package com.pmd.rentavehiculos.viewModels


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmd.rentavehiculos.modelo.RentaVehiculo
import com.pmd.rentavehiculos.modelo.Vehiculo
import com.pmd.rentavehiculos.repositorio.RentaRepository
import kotlinx.coroutines.launch

class VehiculosViewModel : ViewModel() {

    private val repository = RentaRepository()

    // LiveData para la lista de vehículos disponibles
    val vehiculosDisponiblesLiveData = MutableLiveData<List<Vehiculo>>()
    val vehiculosRentadosLiveData = MutableLiveData<List<RentaVehiculo>>()
    // LiveData para errores
    val errorLiveData = MutableLiveData<String>()

    // Función para obtener vehículos disponibles
    // La API key se pasa como parámetro para mayor flexibilidad
    fun obtenerVehiculosDisponibles(apiKey: String) {
        viewModelScope.launch {
            try {
                val vehiculos = repository.obtenerVehiculosDisponibles(apiKey)
                vehiculosDisponiblesLiveData.value = vehiculos
            } catch (ex: Exception) {
                errorLiveData.value = "Error al obtener vehículos: ${ex.message}"
            }
        }
    }

    fun rentarVehiculo(apiKey: String, vehiculoId: Int, personaId: Int, dias: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                // Crea un objeto RentarVehiculoRequest con los datos requeridos.
                // Completa los datos (por ejemplo, la información de la persona y otros campos)
                val request = com.pmd.rentavehiculos.modelo.RentarVehiculoRequest(
                    vehiculoId = vehiculoId,
                    dias = dias
                )
                repository.reservarVehiculo(apiKey, vehiculoId, request)
                onSuccess()
            } catch (ex: Exception) {
                errorLiveData.value = "Error al rentar el vehículo: ${ex.message}"
            }
        }
    }


    fun obtenerVehiculosRentados(apiKey: String, personaId: Int) {
        viewModelScope.launch {
            try {
                val rentas = repository.obtenerVehiculosRentados(apiKey, personaId)
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
                // Puedes actualizar la lista luego de entregar
            } catch (ex: Exception) {
                errorLiveData.value = "Error al entregar vehículo: ${ex.message}"
            }
        }
    }
}