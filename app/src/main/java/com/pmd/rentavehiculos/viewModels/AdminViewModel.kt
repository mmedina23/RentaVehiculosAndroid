package com.pmd.rentavehiculos.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmd.rentavehiculos.modelo.RentaVehiculo
import com.pmd.rentavehiculos.modelo.Vehiculo
import com.pmd.rentavehiculos.repositorio.RentaRepository
import kotlinx.coroutines.launch

class AdminViewModel : ViewModel() {

    private val repository = RentaRepository()

    val vehiculosDisponiblesLiveData = MutableLiveData<List<Vehiculo>>()
    val rentasLiveData = MutableLiveData<List<RentaVehiculo>>()
    val errorLiveData = MutableLiveData<String>()

    fun obtenerVehiculosDisponibles(apiKey: String) {
        viewModelScope.launch {
            try {
                Log.d("AdminViewModel", "Obteniendo vehículos disponibles...")
                val vehiculos = repository.obtenerVehiculosDisponibles(apiKey)
                vehiculosDisponiblesLiveData.value = vehiculos
                Log.d("AdminViewModel", "Vehículos disponibles obtenidos: ${vehiculos.size}")
            } catch (ex: Exception) {
                errorLiveData.value = "Error: ${ex.message}"
                Log.e("AdminViewModel", "Error al obtener vehículos disponibles", ex)
            }
        }
    }

    fun obtenerTodasLasRentas(apiKey: String) {
        viewModelScope.launch {
            try {
                val rentas = repository.obtenerTodasLasRentas(apiKey)
                rentasLiveData.value = rentas
            } catch (ex: Exception) {
                errorLiveData.value = "Error al obtener rentas: ${ex.message}"
            }
        }
    }

    // Aquí podrías agregar otras funciones, como crear, actualizar o eliminar vehículos.
}