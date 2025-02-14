package com.pmd.rentavehiculos.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pmd.rentavehiculos.modelos.Vehiculo
import com.pmd.rentavehiculos.retrofit.ApiService
import kotlinx.coroutines.launch

class ListaVehiculosViewModel(
    private val apiService: ApiService
) : ViewModel() {
    class Factory(private val apiService: ApiService) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ListaVehiculosViewModel::class.java)) {
                return ListaVehiculosViewModel(apiService) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }


    private var apiKey: String = ""
    private var personaId: Int = -1

    private val _vehiculosDisponibles = MutableLiveData<List<Vehiculo>>()
    val vehiculosDisponibles: LiveData<List<Vehiculo>> = _vehiculosDisponibles

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage


    // ✅ Método para establecer la apiKey y el ID de la persona
    fun setCredentials(apiKey: String, personaId: Int) {
        this.apiKey = apiKey
        this.personaId = personaId
        Log.d("API_DEBUG", "API Key en ListaVehiculosViewModel: $apiKey")
    }

    fun fetchVehiculosDisponibles() {
        viewModelScope.launch {
            try {
                if (apiKey.isEmpty()) {
                    _errorMessage.postValue("Error: apiKey está vacía en ListaVehiculosViewModel")
                    Log.e("API_DEBUG", "Error: apiKey está vacía en ListaVehiculosViewModel")
                    return@launch
                }
                val vehiculos = apiService.getVehiculos(apiKey)
                _vehiculosDisponibles.postValue(vehiculos.filter { it.disponible })
            } catch (e: Exception) {
                _errorMessage.postValue("Error al obtener vehículos: ${e.message}")
                Log.e("API_DEBUG", "Error en fetchVehiculosDisponibles: ${e.message}")
            }
        }
    }
}


