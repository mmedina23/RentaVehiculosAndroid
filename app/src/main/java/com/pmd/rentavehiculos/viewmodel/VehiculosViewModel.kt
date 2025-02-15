package com.pmd.rentavehiculos.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmd.rentavehiculos.model.Vehiculo
import com.pmd.rentavehiculos.model.Persona
import com.pmd.rentavehiculos.model.RentaRequest
import com.pmd.rentavehiculos.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

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
                Log.e("VehiculosViewModel", "❌ Error: API Key vacía")
                return@launch
            }

            try {
                Log.d("VehiculosViewModel", "🔑 Usando API Key: $apiKey para obtener vehículos disponibles")
                val vehiculosObtenidos = RetrofitClient.vehiculosService.obtenerVehiculos(apiKey, "disponibles")
                _vehiculosDisponibles.value = vehiculosObtenidos
                Log.d("VehiculosViewModel", "✅ Vehículos cargados (${vehiculosObtenidos.size})")
            } catch (e: Exception) {
                Log.e("VehiculosViewModel", "❌ Error al obtener vehículos: ${e.message}")
                _vehiculosDisponibles.value = emptyList()
            }
        }
    }


    /**
     * Obtiene los vehículos rentados por un usuario.
     */
    fun obtenerVehiculosRentados(apiKey: String, personaId: Int) {
        viewModelScope.launch {
            if (apiKey.isBlank()) {
                Log.e("VehiculosViewModel", "❌ Error: API Key vacía")
                return@launch
            }

            Log.d("VehiculosViewModel", "🔑 Usando API Key: $apiKey")

            try {
                val rentasObtenidas = RetrofitClient.vehiculosService.obtenerVehiculosRentados("Bearer $apiKey", personaId)

                rentas.value = emptyList()
                rentas.value = rentasObtenidas
                Log.d("VehiculosViewModel", "✅ Vehículos rentados obtenidos (${rentas.value.size})")

            } catch (e: HttpException) {
                if (e.code() == 403) {
                    Log.e("VehiculosViewModel", "❌ API Key inválida, intenta iniciar sesión nuevamente.")
                } else {
                    Log.e("VehiculosViewModel", "❌ Error HTTP ${e.code()} - ${e.response()?.errorBody()?.string()}")
                }
            } catch (e: IOException) {
                Log.e("VehiculosViewModel", "❌ Error de conexión: ${e.message}")
            } catch (e: Exception) {
                Log.e("VehiculosViewModel", "❌ Error inesperado: ${e.message}")
            }
        }
    }


    /**
     * Reserva un vehículo para el usuario logueado.
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

            val rentasActivas = rentas.value.filter { it.fecha_estimada_entrega.isNullOrEmpty() }
            Log.d("VehiculosViewModel", "📋 Rentas activas del usuario: ${rentasActivas.size}")

            if (rentasActivas.size >= 3) {
                onResult(false, "No puedes rentar más de 3 vehículos")
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

                val response = RetrofitClient.vehiculosService.reservarVehiculo(apiKey, vehiculo.id, rentaRequest)

                if (response.isSuccessful) {
                    onResult(true, "✅ Reserva exitosa")
                } else {
                    val errorBody = response.errorBody()?.string()
                    onResult(false, "❌ Error en la reserva: $errorBody")
                }

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
            if (apiKey.isBlank()) {
                onResult(false, "Error: apiKey inválida")
                return@launch
            }

            try {
                RetrofitClient.vehiculosService.entregarVehiculo("Bearer $apiKey", vehiculoId)

                val rentaIndex = rentas.value.indexOfFirst { it.vehiculo.id == vehiculoId }
                if (rentaIndex != -1) {
                    val rentaActualizada = rentas.value[rentaIndex].copy(fecha_estimada_entrega = obtenerFechaActual())
                    rentas.value = rentas.value.toMutableList().apply { set(rentaIndex, rentaActualizada) }
                }

                onResult(true, "✅ Vehículo entregado correctamente")

            } catch (e: Exception) {
                Log.e("VehiculosViewModel", "❌ Error al entregar vehículo: ${e.message}")
                onResult(false, "Error al entregar vehículo")
            }
        }
    }

    /**
     * Obtiene la fecha actual en el formato correcto.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun obtenerFechaActual(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        return LocalDateTime.now().format(formatter)
    }

    /**
     * Obtiene la fecha de entrega sumando los días de renta.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun obtenerFechaEntrega(diasRenta: Int): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        return LocalDateTime.now().plusDays(diasRenta.toLong()).format(formatter)
    }

    /**
     * Obtiene el historial de rentas de un vehículo (para admins).
     */
    fun obtenerVehiculosRentadosAdmin(apiKey: String, vehiculoId: Int) {
        viewModelScope.launch {
            if (apiKey.isBlank()) {
                Log.e("VehiculosViewModel", "❌ API Key vacía o inválida")
                return@launch
            }

            isLoading.value = true
            try {
                Log.d("VehiculosViewModel", "🔄 Cargando historial de rentas del vehículo $vehiculoId con API Key: $apiKey")
                val rentasObtenidas = RetrofitClient.vehiculosService.obtenerHistorialRentas(apiKey, vehiculoId)
                rentas.value = rentasObtenidas
                Log.d("VehiculosViewModel", "✅ Historial de rentas obtenido (${rentasObtenidas.size})")
            } catch (e: HttpException) {
                if (e.code() == 403) {
                    Log.e("VehiculosViewModel", "❌ API Key inválida o sin permisos. Intenta iniciar sesión nuevamente.")
                } else {
                    Log.e("VehiculosViewModel", "❌ Error HTTP ${e.code()} - ${e.response()?.errorBody()?.string()}")
                }
            } catch (e: IOException) {
                Log.e("VehiculosViewModel", "❌ Error de conexión: ${e.message}")
            } catch (e: Exception) {
                Log.e("VehiculosViewModel", "❌ Error inesperado: ${e.message}")
            } finally {
                isLoading.value = false
            }
        }
    }


    /**
     * liberar los vehiculos .
     */

    fun liberarVehiculo(apiKey: String, vehiculoId: Int, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            if (apiKey.isBlank()) {
                onResult(false, "❌ Error: API Key inválida")
                return@launch
            }

            try {
                val response = RetrofitClient.vehiculosService.entregarVehiculo(apiKey, vehiculoId)

                if (response.isSuccessful) {
                    Log.d("VehiculosViewModel", "✅ Vehículo liberado correctamente")

                    // Recargar lista de vehículos disponibles
                    obtenerVehiculosDisponibles(apiKey)

                    onResult(true, "✅ Vehículo liberado correctamente")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("VehiculosViewModel", "❌ Error al liberar vehículo: $errorBody")
                    onResult(false, "❌ Error al liberar vehículo: $errorBody")
                }

            } catch (e: HttpException) {
                Log.e("VehiculosViewModel", "❌ Error HTTP ${e.code()} - ${e.response()?.errorBody()?.string()}")
                onResult(false, "❌ Error HTTP ${e.code()}")
            } catch (e: IOException) {
                Log.e("VehiculosViewModel", "❌ Error de conexión con el servidor")
                onResult(false, "❌ Error de conexión con el servidor")
            } catch (e: Exception) {
                Log.e("VehiculosViewModel", "❌ Error desconocido: ${e.message}")
                onResult(false, "❌ Error desconocido")
            }
        }
    }


}
