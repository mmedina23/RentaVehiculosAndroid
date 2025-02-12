package com.pmd.rentavehiculos.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.pmd.rentavehiculos.models.VehiculoRentado
import com.pmd.rentavehiculos.network.ApiClient
import com.pmd.rentavehiculos.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VehiculosRentadosViewModel : ViewModel() {
    private val apiService: ApiService = ApiClient.retrofit.create(ApiService::class.java)

    var vehiculosRentados = mutableStateListOf<VehiculoRentado>()
        private set

    var isLoading by mutableStateOf(true)
        private set

    // ✅ Cargar vehículos rentados con personaId

    fun cargarVehiculosRentados(token: String, personaId: Int) { // ✅ Se agrega `personaId`
        isLoading = true
        val call =
            apiService.obtenerVehiculosRentados(token, personaId) // ✅ Ahora se pasa `personaId`
        call.enqueue(object : Callback<List<VehiculoRentado>> {
            override fun onResponse(
                call: Call<List<VehiculoRentado>>,
                response: Response<List<VehiculoRentado>>
            ) {
                if (response.isSuccessful) {
                    vehiculosRentados.clear()
                    response.body()?.let { vehiculosRentados.addAll(it) }
                    Log.d(
                        "VehiculosRentadosVM",
                        "✅ Vehículos rentados cargados: ${vehiculosRentados.size}"
                    )
                } else {
                    Log.e(
                        "VehiculosRentadosVM",
                        "❌ Error al obtener vehículos rentados: ${response.errorBody()?.string()}"
                    )
                }
                isLoading = false
            }

            override fun onFailure(call: Call<List<VehiculoRentado>>, t: Throwable) {
                Log.e("VehiculosRentadosVM", "❌ Error de conexión: ${t.message}")
                isLoading = false
            }
        })
    }
}