package com.pmd.rentavehiculos.viewmodel

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.pmd.rentavehiculos.model.Persona
import com.pmd.rentavehiculos.model.Vehiculo
import com.pmd.rentavehiculos.model.RentaRequest
import com.pmd.rentavehiculos.notification.EntregaNotificacionWorker
import com.pmd.rentavehiculos.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.TimeUnit

class VehiculosViewModel : ViewModel() {

    private val _vehiculosDisponibles = MutableStateFlow<List<Vehiculo>>(emptyList())
    val vehiculosDisponibles: StateFlow<List<Vehiculo>> = _vehiculosDisponibles

    val rentas = MutableStateFlow<List<RentaRequest>>(emptyList())
    var isLoading = MutableStateFlow(false)

    /**
     * Obtiene la lista de vehículos disponibles desde la API.
     */
    fun obtenerVehiculosDisponibles(apiKey: String) {
        viewModelScope.launch {
            if (apiKey.isBlank()) {
                Log.e("VehiculosViewModel", "❌ API Key vacía")
                return@launch
            }

            try {
                val vehiculosObtenidos =
                    RetrofitClient.vehiculosService.obtenerVehiculos(apiKey, "disponibles")
                _vehiculosDisponibles.value = vehiculosObtenidos
                Log.d("VehiculosViewModel", "✅ Vehículos obtenidos: ${vehiculosObtenidos.size}")
            } catch (e: Exception) {
                Log.e("VehiculosViewModel", "❌ Error al obtener vehículos: ${e.message}")
                _vehiculosDisponibles.value = emptyList()
            }
        }
    }

    /**
     * Obtiene los vehículos rentados por un usuario.
     */

    @RequiresApi(Build.VERSION_CODES.O)
    fun obtenerVehiculosRentados(apiKey: String, personaId: Int, context: Context) {
        viewModelScope.launch {
            if (apiKey.isBlank()) {
                Log.e("VehiculosViewModel", "❌ API Key vacía")
                return@launch
            }

            try {
                Log.d(
                    "VehiculosViewModel",
                    "📡 Obteniendo vehículos rentados por Persona ID: $personaId"
                )
                val rentasObtenidas =
                    RetrofitClient.vehiculosService.obtenerVehiculosRentados(apiKey, personaId)
                rentas.value = rentasObtenidas

                // 🔔 Programar notificación para vehículos próximos a entrega
                rentasObtenidas.forEach { renta ->
                    if (!renta.fecha_estimada_entrega.isNullOrEmpty()) {
                        val fechaEntrega = LocalDate.parse(
                            renta.fecha_estimada_entrega,
                            DateTimeFormatter.ISO_DATE_TIME
                        )
                        val hoy = LocalDate.now()
                        val diasRestantes = hoy.until(fechaEntrega).days

                        if (diasRestantes in 0..2) { // Si la entrega es hoy o en los próximos 2 días
                            programarNotificacion(
                                context,
                                renta.vehiculo.marca,
                                renta.fecha_estimada_entrega
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("VehiculosViewModel", "❌ Error al obtener vehículos rentados: ${e.message}")
                rentas.value = emptyList()
            }
        }
    }

    /**
     * Obtiene el historial de rentas de un vehículo (para admins).
     */
    fun obtenerHistorialRentasAdmin(apiKey: String, vehiculoId: Int) {
        viewModelScope.launch {
            try {
                val rentasObtenidas =
                    RetrofitClient.vehiculosService.obtenerHistorialRentas(apiKey, vehiculoId)
                rentas.value = rentasObtenidas
                Log.d(
                    "VehiculosViewModel",
                    "📋 Historial de rentas para vehículo ID $vehiculoId: ${rentasObtenidas.size}"
                )
            } catch (e: Exception) {
                Log.e("VehiculosViewModel", "❌ Error al obtener historial de rentas: ${e.message}")
                rentas.value = emptyList()
            }
        }
    }

    /**
     * Libera un vehículo rentado (tanto para Cliente como Admin).
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun liberarVehiculo(apiKey: String, vehiculoId: Int, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            if (apiKey.isBlank()) {
                onResult(false, "Error: API Key inválida")
                return@launch
            }

            try {
                Log.d("VehiculosViewModel", "📡 Liberando vehículo con ID: $vehiculoId")
                val response = RetrofitClient.vehiculosService.entregarVehiculo(apiKey, vehiculoId)

                if (response.isSuccessful) {
                    onResult(true, "✅ Vehículo liberado correctamente")
                } else {
                    onResult(false, "❌ Error al liberar vehículo")
                }
            } catch (e: Exception) {
                onResult(false, "❌ Error inesperado: ${e.message}")
            }
        }
    }


    /**
     * Programa una notificación para la entrega del vehículo.
     */
    fun programarNotificacion(context: Context, vehiculoNombre: String, fechaEntrega: String) {
        val inputData = Data.Builder()
            .putString("vehiculo_nombre", vehiculoNombre)
            .putString("fecha_entrega", fechaEntrega)
            .build()

        val request = OneTimeWorkRequestBuilder<EntregaNotificacionWorker>()
            .setInitialDelay(1, TimeUnit.HOURS) // ⏳ Ajusta según necesidad
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(context).enqueue(request)

        Log.d(
            "VehiculosViewModel",
            "🔔 Notificación programada para $vehiculoNombre el $fechaEntrega"
        )
    }

    /**
     * Reservar vehiculos
     */

    @RequiresApi(Build.VERSION_CODES.O)
    fun reservarVehiculo(
        apiKey: String?,
        usuario: Persona?,
        vehiculo: Vehiculo,
        diasRenta: Int,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            if (apiKey.isNullOrEmpty()) {
                onResult(false, "Error: API Key inválida")
                return@launch
            }
            if (usuario == null) {
                onResult(false, "Error: Usuario no autenticado")
                return@launch
            }

            try {
                val fechaRenta = obtenerFechaActual()
                val fechaEntrega = obtenerFechaEntrega(diasRenta)

                val rentaRequest = RentaRequest(
                    persona = usuario,
                    vehiculo = vehiculo,
                    dias_renta = diasRenta,
                    valor_total_renta = vehiculo.valor_dia * diasRenta,
                    fecha_renta = fechaRenta,
                    fecha_estimada_entrega = fechaEntrega
                )

                Log.d("VehiculosViewModel", "📩 Enviando solicitud de reserva con API Key: $apiKey")
                val response = RetrofitClient.vehiculosService.reservarVehiculo(
                    apiKey,
                    vehiculo.id,
                    rentaRequest
                )

                if (response.isSuccessful) {
                    onResult(true, "✅ Reserva exitosa")
                } else {
                    val errorBody = response.errorBody()?.string()
                    onResult(false, "❌ Error en la reserva: $errorBody")
                }

            } catch (e: Exception) {
                onResult(false, "Error en la reserva: ${e.message}")
            }
        }

    }

    /**
     * fucion para fecha actual
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun obtenerFechaActual(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        return LocalDateTime.now().format(formatter)
    }


    /**
     * fucion para fecha de entrega
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun obtenerFechaEntrega(diasRenta: Int): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        return LocalDateTime.now().plusDays(diasRenta.toLong()).format(formatter)
    }


}