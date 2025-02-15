package com.pmd.rentavehiculos.ui.theme.viewmodel

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.pmd.rentavehiculos.data.model.PersonaRequest
import com.pmd.rentavehiculos.data.model.Renta
import com.pmd.rentavehiculos.data.model.RentarVehiculoRequest
import com.pmd.rentavehiculos.data.model.Vehiculo
import com.pmd.rentavehiculos.data.repository.RentaService
import com.pmd.rentavehiculos.data.repository.SessionManager
import com.pmd.rentavehiculos.data.repository.VehiculoService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.util.Calendar
import java.util.Locale

class ClienteViewModel(
    private val rentaService: RentaService,
    private val vehiculoService: VehiculoService,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _vehiculosDisponibles = MutableStateFlow<List<Vehiculo>>(emptyList())
    val vehiculosDisponibles: StateFlow<List<Vehiculo>> = _vehiculosDisponibles

    private val _vehiculosRentados = MutableStateFlow<List<Renta>>(emptyList())
    val vehiculosRentados: StateFlow<List<Renta>> = _vehiculosRentados

    init {
        cargarVehiculosDisponibles()
        cargarVehiculosRentados()
    }

    /**
     * Obtiene la lista de vehículos disponibles desde la API.
     */
    fun cargarVehiculosDisponibles() {
        viewModelScope.launch {
            try {
                val response = vehiculoService.obtenerVehiculos(sessionManager.token!!, "disponibles")
                if (response.isSuccessful) {
                    _vehiculosDisponibles.value = response.body() ?: emptyList()
                } else {
                    Log.e("ClienteViewModel", "Error obteniendo vehículos disponibles: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("ClienteViewModel", "Excepción obteniendo vehículos disponibles", e)
            }
        }
    }

    /**
     * Obtiene la lista de vehículos rentados por el usuario.
     */
    fun cargarVehiculosRentados() {
        viewModelScope.launch {
            sessionManager.personaId?.let { personaId ->
                try {
                    val response = rentaService.obtenerVehiculosRentados(sessionManager.token!!, personaId)
                    if (response.isSuccessful) {
                        _vehiculosRentados.value = response.body() ?: emptyList()
                    } else {
                        Log.e("ClienteViewModel", "Error obteniendo vehículos rentados: ${response.message()}")
                    }
                } catch (e: Exception) {
                    Log.e("ClienteViewModel", "Excepción obteniendo vehículos rentados", e)
                }
            }
        }
    }

    /**
     * Reserva un vehículo para el usuario logueado.
     */
    fun rentarVehiculo(vehiculo: Vehiculo, onResult: (Boolean, String) -> Unit) {
        if (_vehiculosRentados.value.size >= 3) {
            Log.e("ClienteViewModel", "No puedes rentar más de 3 vehículos.")
            onResult(false, "No puedes rentar más de 3 vehículos a la vez.")
            return
        }

        viewModelScope.launch {
            try {
                val personaId = sessionManager.personaId?.toString() ?: ""
                if (personaId.isEmpty()) {
                    Log.e("ClienteViewModel", "ID de persona no válido")
                    onResult(false, "Error: No se encontró el usuario logueado.")
                    return@launch
                }

                val personaRequest = PersonaRequest(personaId)
                val diasRenta = 5

                // 📌 Validación del valor por día
                val valorPorDia = vehiculo.valor_dia ?: 0.0
                if (valorPorDia <= 0.0) {
                    Log.e("ClienteViewModel", "⚠️ El valor del vehículo es inválido: $valorPorDia")
                    onResult(false, "Error: El vehículo tiene un valor por día inválido.")
                    return@launch
                }

                val valorTotalRenta = valorPorDia * diasRenta

                // 📌 Asegurar que los valores de fechas sean correctos
                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                val calendar = Calendar.getInstance()
                val fechaRenta = dateFormat.format(calendar.time)
                calendar.add(Calendar.DAY_OF_YEAR, diasRenta)
                val fechaEntrega = dateFormat.format(calendar.time)

                // 📌 Validación de valores antes de enviar
                Log.d("ClienteViewModel", "✅ Datos antes de enviar:")
                Log.d("ClienteViewModel", "Persona ID: $personaId")
                Log.d("ClienteViewModel", "Días de renta: $diasRenta")
                Log.d("ClienteViewModel", "Valor por día: $valorPorDia")
                Log.d("ClienteViewModel", "Valor total renta: $valorTotalRenta")
                Log.d("ClienteViewModel", "Fecha renta: $fechaRenta")
                Log.d("ClienteViewModel", "Fecha entrega: $fechaEntrega")

                val rentaRequest = RentarVehiculoRequest(
                    persona = personaRequest,
                    dias_renta = diasRenta,
                    valor_total_renta = valorTotalRenta,
                    fecha_renta = fechaRenta,
                    fecha_estimada_entrega = fechaEntrega
                )

                // 📌 Log para verificar el JSON enviado
                val jsonRequest = Gson().toJson(rentaRequest)
                Log.d("ClienteViewModel", "🚀 Enviando solicitud de renta: $jsonRequest")

                val response = rentaService.reservarVehiculo(sessionManager.token!!, vehiculo.id, rentaRequest)

                if (response.isSuccessful) {
                    Log.d("ClienteViewModel", "✅ Renta exitosa")
                    cargarVehiculosDisponibles()
                    cargarVehiculosRentados()
                    onResult(true, "Reserva exitosa")
                } else {
                    Log.e("ClienteViewModel", "❌ Error al rentar vehículo: ${response.code()} - ${response.message()}")
                    response.errorBody()?.let {
                        Log.e("ClienteViewModel", "Detalles del error: ${it.string()}")
                    }
                    onResult(false, "Error al rentar vehículo: ${response.code()}")
                }
            } catch (e: HttpException) {
                onResult(false, "Error HTTP: ${e.code()}")
            } catch (e: IOException) {
                onResult(false, "Error de conexión con el servidor")
            } catch (e: Exception) {
                onResult(false, "Error desconocido al rentar vehículo")
            }
        }
    }


    /**
     * Entrega un vehículo rentado.
     */
    fun entregarVehiculo(renta: Renta, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = rentaService.entregarVehiculo(sessionManager.token!!, renta.vehiculo.id)
                if (response.isSuccessful) {
                    cargarVehiculosDisponibles()
                    cargarVehiculosRentados()
                    onResult(true, "Vehículo entregado correctamente")
                } else {
                    Log.e("ClienteViewModel", "Error al entregar vehículo: ${response.message()}")
                    onResult(false, "Error al entregar vehículo")
                }
            } catch (e: IOException) {
                onResult(false, "Error de conexión con el servidor")
            } catch (e: Exception) {
                onResult(false, "Error desconocido al entregar vehículo")
            }
        }
    }
}
