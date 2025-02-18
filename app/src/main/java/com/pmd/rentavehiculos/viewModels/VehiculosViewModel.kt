package com.pmd.rentavehiculos.viewModels

import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmd.rentavehiculos.modelo.Persona
import com.pmd.rentavehiculos.modelo.RentaRequest
import com.pmd.rentavehiculos.modelo.Vehiculo
import com.pmd.rentavehiculos.retrofit.RetrofitClient
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

/**
 * ViewModel encargado de gestionar la información y operaciones sobre los vehículos y rentas.
 */
class VehiculosViewModel : ViewModel() {

    // Listas de vehículos y rentas
    val vehiculos = mutableStateListOf<Vehiculo>()
    val vehiculosDisponibles = mutableStateListOf<Vehiculo>()
    val rentas = mutableStateListOf<RentaRequest>()

    /**
     * Obtiene la lista de vehículos disponibles desde la API.
     */
    fun obtenerVehiculos(apiKey: String) {
        viewModelScope.launch {
            try {
                val vehiculosObtenidos =
                    RetrofitClient.vehiculosService.obtenerVehiculos(apiKey, "disponibles")
                vehiculos.clear()
                vehiculos.addAll(vehiculosObtenidos)
            } catch (e: Exception) {
                vehiculos.clear() // Limpia la lista en caso de error
            }
        }
    }

    /**
     * Obtiene los vehículos rentados por un usuario específico.
     */
    fun obtenerVehiculosRentados(apiKey: String, personaId: Int) {
        viewModelScope.launch {
            try {
                val rentasObtenidas =
                    RetrofitClient.vehiculosService.obtenerVehiculosRentados(apiKey, personaId)
                rentas.clear()
                rentas.addAll(rentasObtenidas)
            } catch (e: Exception) {
                rentas.clear()
            }
        }
    }

    /**
     * Obtiene el historial de rentas de un vehículo para administradores.
     */
    fun obtenerVehiculosRentadosAdmin(apiKey: String, vehiculoId: Int) {
        viewModelScope.launch {
            try {
                val rentasObtenidas =
                    RetrofitClient.vehiculosService.obtenerHistorialRentas(apiKey, vehiculoId)
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
                val vehiculosObtenidos =
                    RetrofitClient.vehiculosService.obtenerVehiculos(apiKey, "disponibles")
                // Imprimir la respuesta completa de la API
                println("Respuesta completa de la API: $vehiculosObtenidos")
                vehiculosDisponibles.clear()
                vehiculosDisponibles.addAll(vehiculosObtenidos)

                // Imprimir cada vehículo recibido
                vehiculos.forEach { vehiculo ->
                    println("Vehículo recibido: $vehiculo")
                    println("Marca: ${vehiculo.marca}")
                    println("Imagen URL: ${vehiculo.imagen}")  // ← Imprime la URL de la imagen
                }

            } catch (e: Exception) {
                vehiculosDisponibles.clear()
            }
        }
    }

    /**
     * Reserva un vehículo para el usuario logueado.
     */
    fun reservarVehiculo(
        apiKey: String,
        usuario: Persona?,
        vehiculo: Vehiculo,
        diasRenta: Int,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (usuario == null) {
                    onResult(false, "Error: Usuario no identificado")
                    return@launch
                }

                // Obtener la lista actualizada de vehículos rentados
                val rentasActualizadas =
                    RetrofitClient.vehiculosService.obtenerVehiculosRentados(apiKey, usuario.id)
                rentas.clear()
                rentas.addAll(rentasActualizadas)

                // Verificar si el usuario ya tiene 3 vehículos rentados activos
                val vehiculosRentados = rentas.filter { it.fecha_entregado.isEmpty() }
                if (vehiculosRentados.size >= 3) {
                    onResult(false, "No puedes rentar más de 3 vehículos a la vez.")
                    return@launch
                }

                // Generar fechas para la renta
                val dateFormat =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val calendar = Calendar.getInstance()
                val fechaRenta = dateFormat.format(calendar.time) // Fecha actual
                calendar.add(Calendar.DAY_OF_YEAR, diasRenta)
                val fechaEntrega = dateFormat.format(calendar.time) // Fecha estimada de entrega

                // Crear el objeto de solicitud de renta
                val rentaRequest = RentaRequest(
                    persona = usuario,
                    vehiculo = vehiculo,
                    dias_renta = diasRenta,
                    valor_total_renta = vehiculo.valor_dia * diasRenta,
                    fecha_renta = fechaRenta,
                    fecha_entregado = "",
                    fecha_estimada_entrega = fechaEntrega
                )

                // Realizar la reserva en la API
                RetrofitClient.vehiculosService.reservarVehiculo(apiKey, vehiculo.id, rentaRequest)

                // Eliminar el vehículo de la lista de disponibles
                vehiculos.removeIf { it.id == vehiculo.id }

                // Notificar éxito
                onResult(true, "Reserva realizada con éxito")

                // Actualizar la lista de vehículos rentados del usuario
                obtenerVehiculosRentados(apiKey, usuario.id)

            } catch (e: Exception) {
                onResult(false, "Error en la reserva")
            }
        }
    }

    /**
     * Registra la entrega de un vehículo rentado.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun entregarVehiculo(apiKey: String, vehiculoId: Int, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                // Enviar la solicitud de entrega a la API
                RetrofitClient.vehiculosService.entregarVehiculo(apiKey, vehiculoId)

                // Buscar la renta asociada al vehículo y actualizar su estado
                val rentaIndex = rentas.indexOfFirst { it.vehiculo.id == vehiculoId }
                if (rentaIndex != -1) {
                    val rentaActualizada =
                        rentas[rentaIndex].copy(fecha_entregado = obtenerFechaActual())
                    rentas[rentaIndex] = rentaActualizada
                }

                onResult(true, "Vehículo entregado correctamente")

            } catch (e: Exception) {
                onResult(false, "Error al entregar el vehículo")
            }
        }
    }

    /**
     * Obtiene la fecha actual formateada en `yyyy-MM-dd HH:mm:ss`.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun obtenerFechaActual(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return LocalDateTime.now().format(formatter)
    }
}
