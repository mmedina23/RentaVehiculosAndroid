package com.pmd.rentavehiculos.ui.theme.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmd.rentavehiculos.data.RetrofitService
import com.pmd.rentavehiculos.data.RetrofitServiceFactory
import com.pmd.rentavehiculos.data.model.Renta
import com.pmd.rentavehiculos.data.model.Vehiculo
import kotlinx.coroutines.launch

class AdminViewModel : ViewModel() {
    private val retrofitService: RetrofitService = RetrofitServiceFactory.RetrofitService()

    private val _vehiculosDisponibles = MutableLiveData<List<Vehiculo>>()
    val vehiculosDisponibles: LiveData<List<Vehiculo>> get() = _vehiculosDisponibles

    private val _vehiculosRentados = MutableLiveData<List<Renta>>()
    val vehiculosRentados: LiveData<List<Renta>> get() = _vehiculosRentados

    // Clave API fija para prueba
    var llaveApi: String? = "clave_api_valida"

    fun loadVehiculosDisponibles() {
        viewModelScope.launch {
            try {
                llaveApi?.let {
                    println("Enviando solicitud con llave: $it")
                    val response = retrofitService.listVehiculos(it, "disponibles")
                    _vehiculosDisponibles.postValue(response)
                } ?: println("Error: Llave API no disponible")
            } catch (e: Exception) {
                println("Error al cargar vehículos disponibles: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun loadVehiculosRentados() {
        viewModelScope.launch {
            try {
                llaveApi?.let {
                    println("Enviando solicitud con llave: $it")
                    val response = retrofitService.getRentasPorVehiculo(it)
                    _vehiculosRentados.postValue(response)
                } ?: println("Error: Llave API no disponible")
            } catch (e: Exception) {
                println("Error al cargar vehículos rentados: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}

