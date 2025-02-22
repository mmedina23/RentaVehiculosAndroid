package com.pmd.rentavehiculos.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.pmd.rentavehiculos.classes.Persona
import com.pmd.rentavehiculos.classes.RentRequest
import com.pmd.rentavehiculos.classes.Vehiculo
import com.pmd.rentavehiculos.remote.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class VehiculosViewModel : ViewModel() {
    var vehiculosDisponibles = mutableStateOf<List<Vehiculo>>(emptyList())
    var rentasCliente = mutableStateOf<List<RentRequest>>(emptyList())
    var historialRenta = mutableStateOf<List<RentRequest>>(emptyList())
    var error = mutableStateOf<String?>(null)

    fun fetchVehiculosDisponibles(key: String) {
        viewModelScope.launch {
            try {
                vehiculosDisponibles.value = RetrofitClient.vehiculosService.getVehiculos(key)
            } catch (e: Exception) {
                error.value = "Error en fetchVehiculosDisponibles: ${e.localizedMessage}"
            }
        }
    }

    fun fetchRentasCliente(key: String, pId: Int) {
        viewModelScope.launch {
            try {
                rentasCliente.value = RetrofitClient.vehiculosService.getRentasPersona(key, pId)
            } catch (e: Exception) {
                error.value = "Error en fetchRentasCliente: ${e.localizedMessage}"
            }
        }
    }

    fun fetchHistorial(key: String, vhId: Int) {
        viewModelScope.launch {
            try {
                historialRenta.value = RetrofitClient.vehiculosService.getHistorial(key, vhId)
            } catch (e: Exception) {
                error.value = "Error en fetchHistorial: ${e.localizedMessage}"
            }
        }
    }

    fun rentarVehiculo(key: String, persona: Persona, vehiculo: Vehiculo, diasRenta: Int, totalRenta: Int, entregaEstimada: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {

                val rentasActuales = RetrofitClient.vehiculosService.getRentasPersona(key, persona.id)

                // Check if the user already has 3 rented vehicles
                if (rentasActuales.size >= 3) {
                    onResult(false, "No puedes rentar más de 3 vehículos a la vez.")
                    return@launch
                }

                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                sdf.timeZone = TimeZone.getTimeZone("UTC")
                val fechaRenta = sdf.format(Date())
                val renta = RentRequest(
                    persona = persona,
                    vehiculo = vehiculo,
                    dias_renta = diasRenta,
                    valor_total_renta = totalRenta,
                    fecha_renta = fechaRenta,
                    fecha_estimada_entrega = entregaEstimada,
                    fecha_entregado = ""
                )
                RetrofitClient.vehiculosService.rentarVehiculo(key, vehiculo.id, renta)

                onResult(true, "Vehiculo Rentado con éxito")
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string() ?: "Error desconocido"
                onResult(false, "HTTP Error ${e.code()}: ${errorBody}")
            } catch (e: IOException) {
                onResult(false, "Error de conexión: ${e.localizedMessage}")
            } catch (e: Exception) {
                onResult(false, "Error: ${e.localizedMessage}")
            }
        }
    }

    fun liberarVehiculo(key:String, vhId: Int, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {

                RetrofitClient.vehiculosService.liberarVehiculo(key, vhId)

                onResult(true, "Vehiculo Liberado con éxito")
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string() ?: "Error desconocido"
                onResult(false, "HTTP Error ${e.code()}: ${errorBody}")
            } catch (e: IOException) {
                onResult(false, "Error de conexión: ${e.localizedMessage}")
            } catch (e: Exception) {
                onResult(false, "Error: ${e.localizedMessage}")
            }
        }
    }

}