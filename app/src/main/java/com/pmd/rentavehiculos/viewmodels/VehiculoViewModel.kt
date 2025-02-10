package com.pmd.rentavehiculos.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmd.rentavehiculos.network.ApiClient
import com.pmd.rentavehiculos.network.ApiService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.pmd.rentavehiculos.models.RentaRequest
import com.pmd.rentavehiculos.models.RentaResponse
import com.pmd.rentavehiculos.models.Vehiculo

class VehiculoViewModel : ViewModel() {
    private val apiService: ApiService = ApiClient.retrofit.create(ApiService::class.java)

    var vehiculos = mutableStateListOf<Vehiculo>()
        private set

    var isLoading by mutableStateOf(true)
        private set

    fun cargarVehiculos() {
        isLoading = true
        apiService.getVehiculosDisponibles().enqueue(object : Callback<List<Vehiculo>> {
            override fun onResponse(call: Call<List<Vehiculo>>, response: Response<List<Vehiculo>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        vehiculos.clear()
                        vehiculos.addAll(it)
                    }
                }
                isLoading = false
            }

            override fun onFailure(call: Call<List<Vehiculo>>, t: Throwable) {
                isLoading = false
            }
        })
    }
    fun rentarVehiculo(usuarioId: Int, vehiculoId: Int, dias: Int, onResult: (String) -> Unit) {
        val apiService = ApiClient.retrofit.create(ApiService::class.java)
        val request = RentaRequest(usuarioId, vehiculoId, dias)

        apiService.rentarVehiculo(request).enqueue(object : Callback<RentaResponse> {
            override fun onResponse(call: Call<RentaResponse>, response: Response<RentaResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onResult("Vehículo rentado por \$$it.totalPago")
                    }
                } else {
                    onResult("Error al rentar vehículo")
                }
            }

            override fun onFailure(call: Call<RentaResponse>, t: Throwable) {
                onResult("Error de conexión")
            }
        })
    }

}
