package com.pmd.rentavehiculos.viewmodel

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmd.rentavehiculos.ClasesPrincipales.Vehiculo
import com.pmd.rentavehiculos.ClasesPrincipales.Persona
import com.pmd.rentavehiculos.ClasesPrincipales.RentaRequest
import com.pmd.rentavehiculos.remote.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class VehiculosViewModel : ViewModel() {

    val vehiculos = mutableStateListOf<Vehiculo>()
    val vehiculosRentados = mutableStateListOf<Vehiculo>()
    val vehiculosDisponibles = mutableStateListOf<Vehiculo>()
    val rentas = mutableStateListOf<RentaRequest>()

    //Obtiene la lista de vehículos disponibles desde la API.

    fun obtenerVehiculos(apiKey: String) {
        viewModelScope.launch {
            try {
                val vehiculosObtenidos = RetrofitClient.vehiculosService.obtenerVehiculos(apiKey, "disponibles")
                vehiculos.clear()
                vehiculos.addAll(vehiculosObtenidos)

            } catch (e: Exception) {
                vehiculos.clear()
            }
        }
    }

    /**
     * Obtiene los vehículos rentados por un usuario.
     */
    fun obtenerVehiculosRentados(apiKey: String, personaId: Int) {
        viewModelScope.launch {
            try {
                val rentasObtenidas = RetrofitClient.vehiculosService.obtenerVehiculosRentados(apiKey, personaId)
                rentas.clear()
                rentas.addAll(rentasObtenidas)
            } catch (e: Exception) {
                rentas.clear()
            }
        }
    }

    /**
     * Obtiene el historial de rentas de un vehículo (para admins).
     */
    fun obtenerVehiculosRentadosAdmin(apiKey: String, vehiculoId: Int) {
        viewModelScope.launch {
            try {
                val rentasObtenidas = RetrofitClient.vehiculosService.obtenerHistorialRentas(apiKey, vehiculoId)
                rentas.clear()
                rentas.addAll(rentasObtenidas)
            } catch (e: Exception) {
                rentas.clear()
            }
        }
    }

    /**
     * Obtiene la lista de vehículos disponibles desde la API.
     */
    fun obtenerVehiculosDisponibles(apiKey: String) {
        viewModelScope.launch {
            try {
                val vehiculosObtenidos = RetrofitClient.vehiculosService.obtenerVehiculos(apiKey, "disponibles")
                vehiculosDisponibles.clear()
                vehiculosDisponibles.addAll(vehiculosObtenidos)
            } catch (e: Exception) {
                vehiculosDisponibles.clear()
            }
        }
    }

    /**
     * Reserva un vehículo para el usuario logueado.
     */
    fun reservarVehiculo(apiKey: String, usuario: Persona?, vehiculo: Vehiculo, diasRenta: Int, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                if (usuario == null) {
                    onResult(false, "Error: No se encontró el usuario logueado")
                    return@launch
                }

                val rentasActualizadas = RetrofitClient.vehiculosService.obtenerVehiculosRentados(apiKey, usuario.id)
                rentas.clear()
                rentas.addAll(rentasActualizadas)

                val vehiculosRentados = rentas.filter { it.fecha_entregado.isNullOrEmpty() }

                if (vehiculosRentados.size >= 3) {
                    onResult(false, "No puedes rentar más de 3 vehículos a la vez.")
                    return@launch
                }

                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val calendar = Calendar.getInstance()
                val fechaRenta = dateFormat.format(calendar.time)
                calendar.add(Calendar.DAY_OF_YEAR, diasRenta)
                val fechaEntrega = dateFormat.format(calendar.time)

                val rentaRequest = RentaRequest(
                    persona = usuario,
                    vehiculo = vehiculo,
                    dias_renta = diasRenta,
                    valor_total_renta = vehiculo.valor_dia * diasRenta,
                    fecha_renta = fechaRenta,
                    fecha_entregado = "",
                    fecha_estimada_entrega = fechaEntrega
                )

                RetrofitClient.vehiculosService.reservarVehiculo(apiKey, vehiculo.id, rentaRequest)

                vehiculos.removeIf { it.id == vehiculo.id }

                onResult(true, "Reserva exitosa")

                obtenerVehiculosRentados(apiKey, usuario.id)

            } catch (e: Exception) {
                onResult(false, "Error en la reserva")
            }
        }
    }

    /**
     * Entrega un vehículo rentado.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun entregarVehiculo(apiKey: String, vehiculoId: Int, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                RetrofitClient.vehiculosService.entregarVehiculo(apiKey, vehiculoId)

                val rentaIndex = rentas.indexOfFirst { it.vehiculo.id == vehiculoId }
                if (rentaIndex != -1) {
                    val rentaActualizada = rentas[rentaIndex].copy(fecha_entregado = obtenerFechaActual())
                    rentas[rentaIndex] = rentaActualizada
                }

                onResult(true, "Vehículo entregado correctamente")

            } catch (e: Exception) {
                onResult(false, "Error al entregar vehículo")
            }
        }
    }

    /**
     * Obtiene la fecha actual formateada.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun obtenerFechaActual(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return LocalDateTime.now().format(formatter)
    }
}
