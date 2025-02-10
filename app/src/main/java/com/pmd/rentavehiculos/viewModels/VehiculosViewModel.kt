package com.pmd.rentavehiculos.viewModels


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmd.rentavehiculos.modelo.Vehiculo
import com.pmd.rentavehiculos.repositorio.RentaRepository
import kotlinx.coroutines.launch

class VehiculosViewModel : ViewModel() {

    private val repository = RentaRepository()

    // LiveData para la lista de vehículos disponibles
    val vehiculosDisponiblesLiveData = MutableLiveData<List<Vehiculo>>()

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
}