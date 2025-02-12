package com.pmd.rentavehiculos.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmd.rentavehiculos.models.*
import com.pmd.rentavehiculos.network.ApiClient
import com.pmd.rentavehiculos.network.ApiService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class VehiculoViewModel : ViewModel() {
    private val apiService: ApiService = ApiClient.retrofit.create(ApiService::class.java)

    var vehiculos = mutableListOf<Vehiculo>()
    var vehiculosRentados = mutableListOf<VehiculoRentado>()
    var isLoading = false

    /** Cargar vehículos disponibles **/
    fun cargarVehiculos(token: String) {
        isLoading = true
        viewModelScope.launch {
            try {
                val response = apiService.obtenerVehiculos(token).execute()
                if (response.isSuccessful) {
                    vehiculos = response.body()?.toMutableList() ?: mutableListOf()
                    Log.d("VehiculoViewModel", "Vehículos cargados: ${vehiculos.size}")
                } else {
                    Log.e("VehiculoViewModel", "Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("VehiculoViewModel", "Error de conexión: ${e.message}")
            }
            isLoading = false
        }
    }

    /** Cargar vehículos rentados **/
    fun cargarVehiculosRentados(token: String, personaId: Int) {
        isLoading = true
        viewModelScope.launch {
            try {
                val response = apiService.obtenerVehiculosRentados(token, personaId).execute()
                if (response.isSuccessful) {
                    vehiculosRentados = response.body()?.toMutableList() ?: mutableListOf()
                    Log.d(
                        "VehiculoViewModel",
                        "Vehículos rentados cargados: ${vehiculosRentados.size}"
                    )
                } else {
                    Log.e("VehiculoViewModel", "Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("VehiculoViewModel", "Error de conexión: ${e.message}")
            }
            isLoading = false
        }
    }

    /** Rentar un vehículo **/
    fun rentarVehiculo(
        token: String,
        persona: Persona,
        vehiculo: Vehiculo,
        diasRenta: Int,
        onResult: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val dateFormat =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val fechaRenta = dateFormat.format(Date())

                val calendar = Calendar.getInstance()
                calendar.add(Calendar.DAY_OF_YEAR, diasRenta)
                val fechaEntrega = dateFormat.format(calendar.time)

                val rentaRequest = RentarVehiculoRequest(
                    persona = persona,
                    vehiculo = vehiculo,
                    diasRenta = diasRenta,
                    valorTotalRenta = vehiculo.valorDia * diasRenta,
                    fechaRenta = fechaRenta,
                    fechaEstimadaEntrega = fechaEntrega
                )

                Log.d("RentarVehiculo", "📤 Enviando datos al servidor: $rentaRequest")

                val response = apiService.rentarVehiculo(token, vehiculo.id, rentaRequest).execute()

                if (response.isSuccessful) {
                    Log.d("RentarVehiculo", "✅ Renta exitosa para vehículo ID: ${vehiculo.id}")
                    onResult(true, null)
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("RentarVehiculo", "❌ Error HTTP ${response.code()}: $errorBody")
                    onResult(false, "Error HTTP ${response.code()}: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("RentarVehiculo", "❌ Error de conexión: ${e.message}")
                onResult(false, "Error de conexión: ${e.message}")
            }
        }
    }
}