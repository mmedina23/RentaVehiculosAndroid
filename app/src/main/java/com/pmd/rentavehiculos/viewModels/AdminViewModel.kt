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
    val vehiculosRentadosAdminLiveData = MutableLiveData<List<Vehiculo>>()

    fun obtenerVehiculosRentadosAdmin(apiKey: String) {
        viewModelScope.launch {
            try {
                val vehiculos = repository.obtenerVehiculosRentadosAdmin(apiKey)
                vehiculosRentadosAdminLiveData.value = vehiculos
            } catch (ex: Exception) {
                errorLiveData.value = "Error al obtener vehículos rentados: ${ex.message}"
            }
        }
    }

    fun obtenerHistorialRentas(apiKey: String, vehiculoId: Int) {
        viewModelScope.launch {
            try {
                // Asegúrate de haber implementado esta función en el repositorio
                val historial = repository.obtenerHistorialRentas(apiKey, vehiculoId)
                rentasLiveData.value = historial
            } catch (ex: Exception) {
                errorLiveData.value = "Error al obtener historial de rentas: ${ex.message}"
            }
        }
    }


    // Aquí podría agregar otras funciones, como crear, actualizar o eliminar vehículos.
}