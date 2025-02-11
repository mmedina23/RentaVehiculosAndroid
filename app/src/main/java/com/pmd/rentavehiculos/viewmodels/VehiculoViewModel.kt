package com.pmd.rentavehiculos.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.pmd.rentavehiculos.models.RentarVehiculoRequest
import com.pmd.rentavehiculos.models.RentarVehiculoResponse
import com.pmd.rentavehiculos.models.Vehiculo
import com.pmd.rentavehiculos.network.ApiClient
import com.pmd.rentavehiculos.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VehiculoViewModel : ViewModel() {
    private val apiService: ApiService = ApiClient.retrofit.create(ApiService::class.java)

    var vehiculos = mutableStateListOf<Vehiculo>()
        private set

    var isLoading by mutableStateOf(true)
        private set

    // ✅ Cargar vehículos disponibles
    fun cargarVehiculos(token: String) {
        isLoading = true
        val call = apiService.obtenerVehiculos(token) // ✅ Función correcta
        call.enqueue(object : Callback<List<Vehiculo>> {
            override fun onResponse(
                call: Call<List<Vehiculo>>,
                response: Response<List<Vehiculo>>
            ) {
                if (response.isSuccessful) {
                    vehiculos.clear()
                    response.body()
                        ?.let { vehiculos.addAll(it) } // ✅ Se usa `addAll()` en lugar de `postValue()`
                    Log.d("VehiculoViewModel", "🚗 Vehículos cargados: ${vehiculos.size}")
                } else {
                    Log.e(
                        "VehiculoViewModel",
                        "❌ Error al obtener vehículos: ${response.errorBody()?.string()}"
                    )
                }
                isLoading = false
            }

            override fun onFailure(call: Call<List<Vehiculo>>, t: Throwable) {
                Log.e("VehiculoViewModel", "❌ Error de conexión: ${t.message}")
                isLoading = false
            }
        })
    }

    // ✅ Renta un vehículo

    fun rentarVehiculo(
        token: String,
        vehiculoId: Int,
        identificacionPersona: String, // ✅ Se necesita la identificación de la persona
        dias: Int,
        onResult: (Boolean, String?) -> Unit
    ) {
        val apiService = ApiClient.retrofit.create(ApiService::class.java)

        // ✅ Convertimos `RentarVehiculoRequest` a un Map<String, Any>
        val requestBody = mapOf(
            "persona" to mapOf("identificacion" to identificacionPersona),
            "dias_renta" to dias
        )

        println("📢 Enviando renta: Vehículo ID: $vehiculoId, Días: $dias, Token: $token")
        println("📢 JSON enviado: $requestBody")

        val call = apiService.rentarVehiculo(token, vehiculoId, requestBody)
        call.enqueue(object : Callback<RentarVehiculoResponse> {
            override fun onResponse(
                call: Call<RentarVehiculoResponse>,
                response: Response<RentarVehiculoResponse>
            ) {
                if (response.isSuccessful) {
                    println("✅ Vehículo rentado con éxito")
                    onResult(true, "Vehículo rentado con éxito")
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Error desconocido"
                    println("❌ Error al rentar: $errorMsg")
                    onResult(false, errorMsg)
                }
            }

            override fun onFailure(call: Call<RentarVehiculoResponse>, t: Throwable) {
                println("❌ Error de conexión: ${t.message}")
                onResult(false, "Error de conexión: ${t.message}")
            }
        })
    }
}
