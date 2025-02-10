package com.pmd.rentavehiculos.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pmd.rentavehiculos.dataModel.Vehiculo
import com.pmd.rentavehiculos.retrofit.RetrofitClient
import com.pmd.rentavehiculos.utils.SessionManager
import kotlinx.coroutines.launch

class VehiculosViewModel(private val sessionManager: SessionManager) : ViewModel() {

    private val _vehiculos = MutableLiveData<List<Vehiculo>>()
    val vehiculos: LiveData<List<Vehiculo>> get() = _vehiculos

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun fetchVehiculos() {
        val apiKey = sessionManager.getApiKey() ?: return
        viewModelScope.launch {
            _loading.postValue(true)
            try {
                val response = RetrofitClient.vehiculosService.obtenerVehiculos(apiKey)
                _vehiculos.postValue(response)
            } catch (e: Exception) {
                _vehiculos.postValue(emptyList())
            } finally {
                _loading.postValue(false)
            }
        }
    }
}

class VehiculosViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VehiculosViewModel::class.java)) {
            val sessionManager = SessionManager(context)
            @Suppress("UNCHECKED_CAST")
            return VehiculosViewModel(sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
