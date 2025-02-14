package com.pmd.rentavehiculos.ui.theme.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmd.rentavehiculos.data.model.Renta
import com.pmd.rentavehiculos.data.model.Vehiculo
import com.pmd.rentavehiculos.data.repository.RetrofitClient
import com.pmd.rentavehiculos.data.repository.SessionManager
import kotlinx.coroutines.launch

class AdminViewModel(context: Context) : ViewModel() {

    private val vehiculoService = RetrofitClient.vehiculoService
    private val rentaService = RetrofitClient.rentaService
    private val sessionManager = SessionManager(context)

    private val _vehiculosDisponibles = MutableLiveData<List<Vehiculo>>()
    val vehiculosDisponibles: LiveData<List<Vehiculo>> get() = _vehiculosDisponibles

    private val _vehiculosRentados = MutableLiveData<List<Renta>>()
    val vehiculosRentados: LiveData<List<Renta>> get() = _vehiculosRentados

    // Método para obtener la llave API (token) desde SessionManager
    private fun obtenerToken(): String? {
        return sessionManager.token
    }

    // Cargar vehículos disponibles
    fun loadVehiculosDisponibles() {
        viewModelScope.launch {
            try {
                val token = obtenerToken()
                if (token != null) {
                    println("Enviando solicitud con token: $token")
                    val response = vehiculoService.obtenerVehiculos(token, "disponibles")
                    if (response.isSuccessful) {
                        _vehiculosDisponibles.postValue(response.body())
                    } else {
                        println("Error: ${response.errorBody()?.string()}")
                    }
                } else {
                    println("Error: Token no disponible")
                }
            } catch (e: Exception) {
                println("Error al cargar vehículos disponibles: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    // Cargar vehículos rentados
    fun loadVehiculosRentados() {
        viewModelScope.launch {
            try {
                val token = obtenerToken()
                if (token != null) {
                    println("Enviando solicitud con token: $token")
                    val response = rentaService.obtenerTodasLasRentas(token)
                    if (response.isSuccessful) {
                        _vehiculosRentados.postValue(response.body())
                    } else {
                        println("Error: ${response.errorBody()?.string()}")
                    }
                } else {
                    println("Error: Token no disponible")
                }
            } catch (e: Exception) {
                println("Error al cargar vehículos rentados: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}
