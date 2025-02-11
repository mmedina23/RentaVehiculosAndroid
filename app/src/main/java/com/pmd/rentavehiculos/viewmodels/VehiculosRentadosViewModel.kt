package com.pmd.rentavehiculos.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.pmd.rentavehiculos.models.DevolverVehiculoResponse
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

    // ✅ Cargar vehículos rentados

    fun cargarVehiculosRentados(token: String) {
        isLoading = true
        val call = apiService.obtenerVehiculosRentados(token) // ✅ Corrección de función
        call.enqueue(object : Callback<List<VehiculoRentado>> {
            override fun onResponse(
                call: Call<List<VehiculoRentado>>,
                response: Response<List<VehiculoRentado>>
            ) {
                if (response.isSuccessful) {
                    vehiculosRentados.clear()
                    response.body()?.let { vehiculosRentados.addAll(it) } // ✅ Se usa addAll()
                    Log.d(
                        "VehiculosRentadosVM",
                        "🚗 Vehículos rentados cargados: ${vehiculosRentados.size}"
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

    // ✅ Devolver vehículo correctamente

    fun devolverVehiculo(token: String, vehiculoId: Int, onResult: (Boolean, String?) -> Unit) {
        val apiService = ApiClient.retrofit.create(ApiService::class.java)

        val call = apiService.devolverVehiculo(token, vehiculoId)
        call.enqueue(object : Callback<Void> { // ✅ Aquí debe ser `Callback<Void>`
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    println("✅ Vehículo devuelto con éxito")
                    onResult(true, "Vehículo devuelto con éxito")
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Error desconocido"
                    println("❌ Error al devolver vehículo: $errorMsg")
                    onResult(false, errorMsg)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                println("❌ Error de conexión: ${t.message}")
                onResult(false, "Error de conexión: ${t.message}")
            }
        })
    }
}

