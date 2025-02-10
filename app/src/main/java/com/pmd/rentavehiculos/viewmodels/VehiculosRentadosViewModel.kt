package com.pmd.rentavehiculos.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmd.rentavehiculos.models.VehiculoRentado
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
import com.pmd.rentavehiculos.models.DevolverVehiculoRequest
import com.pmd.rentavehiculos.models.DevolverVehiculoResponse

class VehiculosRentadosViewModel : ViewModel() {
    private val apiService: ApiService = ApiClient.retrofit.create(ApiService::class.java)

    var vehiculosRentados = mutableStateListOf<VehiculoRentado>()
        private set

    var isLoading by mutableStateOf(true)
        private set

    fun cargarVehiculosRentados(usuarioId: Int) {
        isLoading = true
        apiService.getVehiculosRentados(usuarioId).enqueue(object : Callback<List<VehiculoRentado>> {
            override fun onResponse(call: Call<List<VehiculoRentado>>, response: Response<List<VehiculoRentado>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        vehiculosRentados.clear()
                        vehiculosRentados.addAll(it)
                    }
                }
                isLoading = false
            }

            override fun onFailure(call: Call<List<VehiculoRentado>>, t: Throwable) {
                isLoading = false
            }
        })
    }
    fun devolverVehiculo(usuarioId: Int, vehiculoId: Int, onResult: (String) -> Unit) {
        val apiService = ApiClient.retrofit.create(ApiService::class.java)
        val request = DevolverVehiculoRequest(usuarioId, vehiculoId)

        apiService.devolverVehiculo(request).enqueue(object : Callback<DevolverVehiculoResponse> {
            override fun onResponse(call: Call<DevolverVehiculoResponse>, response: Response<DevolverVehiculoResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onResult(it.mensaje)
                        cargarVehiculosRentados(usuarioId) // Recargar lista después de devolver
                    }
                } else {
                    onResult("Error al devolver el vehículo")
                }
            }

            override fun onFailure(call: Call<DevolverVehiculoResponse>, t: Throwable) {
                onResult("Error de conexión")
            }
        })
    }

}

