package com.pmd.rentavehiculos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pmd.rentavehiculos.model.Vehiculo
import com.pmd.rentavehiculos.remote.ApiClient
import com.pmd.rentavehiculos.remote.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VehiculoViewModel : ViewModel() {
    private val apiService = ApiClient.createService(ApiService::class.java)

    private val _vehiculos = MutableLiveData<List<Vehiculo>>()
    val vehiculos: LiveData<List<Vehiculo>> get() = _vehiculos

    fun cargarVehiculosDisponibles() {
        apiService.obtenerVehiculosDisponibles().enqueue(object : Callback<List<Vehiculo>> {
            override fun onResponse(call: Call<List<Vehiculo>>, response: Response<List<Vehiculo>>) {
                if (response.isSuccessful) {
                    _vehiculos.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<Vehiculo>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}
