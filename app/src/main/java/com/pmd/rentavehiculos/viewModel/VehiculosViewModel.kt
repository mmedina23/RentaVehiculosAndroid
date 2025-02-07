package com.pmd.rentavehiculos.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmd.rentavehiculos.dataModel.Vehiculo
import com.pmd.rentavehiculos.retrofit.RetrofitClient
import kotlinx.coroutines.launch

class VehiculosViewModel : ViewModel() {
    private val _vehiculos = MutableLiveData<List<Vehiculo>>()
    val vehiculos: LiveData<List<Vehiculo>> get() = _vehiculos

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun fetchVehiculos() {
        viewModelScope.launch {
            _loading.postValue(true)
            try {
                val response = RetrofitClient.api.getVehiculos()
                if (response.isSuccessful) {
                    _vehiculos.postValue(response.body())
                }
            } catch (e: Exception) {
                // Manejar errores
            } finally {
                _loading.postValue(false)
            }
        }
    }
}
