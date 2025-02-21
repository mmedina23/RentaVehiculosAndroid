package com.pmd.rentavehiculos.viewmodel

import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmd.rentavehiculos.ClasesPrincipales.Vehiculo
import com.pmd.rentavehiculos.ClasesPrincipales.Persona
import com.pmd.rentavehiculos.ClasesPrincipales.RentaRequest
import com.pmd.rentavehiculos.remote.RetrofitClient
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

/**
 * ViewModel que maneja la lógica de los vehículos en la aplicación.
 */
class VehiculosViewModel : ViewModel() {
    val vehiculos = mutableStateListOf<Vehiculo>()// Lista de vehículos disponibles en el sistema
    val vehiculosRentados = mutableStateListOf<Vehiculo>()// Lista de vehículos que han sido rentados
    val vehiculosDisponibles = mutableStateListOf<Vehiculo>()// Lista de vehículos disponibles para rentar
    val rentas = mutableStateListOf<RentaRequest>()// Lista de rentas realizadas en la aplicación

    /**
     * Obtiene la lista de vehículos disponibles desde la API.
     * Se hace una llamada a RetrofitClient para recuperar los vehículos disponibles.
     */
    fun obtenerVehiculos(apiKey: String) {
        viewModelScope.launch {
            try {
                val vehiculosObtenidos = RetrofitClient.vehiculosService.obtenerVehiculos(apiKey, "disponibles")
                vehiculos.clear() // Limpiar la lista antes de agregar los nuevos datos
                vehiculos.addAll(vehiculosObtenidos)
            } catch (e: Exception) {
                vehiculos.clear() // En caso de error, se limpia la lista
            }
        }
    }

    /**
     * Obtiene los vehículos rentados por un usuario específico.
     * @param apiKey Clave de autenticación de la API.
     * @param personaId Identificador del usuario.
     */
    fun obtenerListadoVehiculos(apiKey: String, personaId: Int) {
        viewModelScope.launch {
            try {
                val rentasObtenidas = RetrofitClient.vehiculosService.obtenerVehiculosRentados(apiKey, personaId)
                rentas.clear()
                rentas.addAll(rentasObtenidas)
            } catch (e: Exception) {
                rentas.clear() // En caso de error, se limpia la lista
            }
        }
    }

    /**
     * Obtiene el historial de rentas de un vehículo (para administradores).
     * @param apiKey Clave de autenticación de la API.
     * @param vehiculoId Identificador del vehículo del cual se quiere conocer el historial.
     */
    fun obtenerListadoVehiculosAdmin(apiKey: String, vehiculoId: Int) {
        viewModelScope.launch {
            try {
                val rentasObtenidas = RetrofitClient.vehiculosService.obtenerHistorialRentas(apiKey, vehiculoId)
                rentas.clear()
                rentas.addAll(rentasObtenidas)
            } catch (e: Exception) {
                rentas.clear() // En caso de error, se limpia la lista
            }
        }
    }

    /**
     * Obtiene la lista de vehículos disponibles desde la API y la almacena en la lista correspondiente.
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
     * @param apiKey Clave de autenticación de la API.
     * @param usuario Persona que está rentando el vehículo.
     * @param vehiculo Vehículo a rentar.
     * @param diasRenta Cantidad de días por los cuales se rentará el vehículo.
     * @param onResult Callback que devuelve un booleano y un mensaje indicando el resultado.
     */
    fun reservarVehiculo(apiKey: String, usuario: Persona?, vehiculo: Vehiculo, diasRenta: Int, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                if (usuario == null) {
                    onResult(false, "Error: No se encontró el usuario")
                    return@launch
                }

                // Obtener la lista de vehículos rentados por el usuario
                val rentasActualizadas = RetrofitClient.vehiculosService.obtenerVehiculosRentados(apiKey, usuario.id)
                rentas.clear()
                rentas.addAll(rentasActualizadas)

                // Verificar si el usuario ha rentado más de 5 vehículos
                val vehiculosRentados = rentas.filter { it.fecha_entrega.isNullOrEmpty() }
                if (vehiculosRentados.size >= 5) {
                    onResult(false, "No puedes rentar más de 5 vehículos a la vez.")
                    return@launch
                }

                // Obtener la fecha actual y calcular la fecha de entrega
                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val calendar = Calendar.getInstance()
                val fechaRenta = dateFormat.format(calendar.time)
                calendar.add(Calendar.DAY_OF_YEAR, diasRenta)
                val fechaEntrega = dateFormat.format(calendar.time)

                // Crear el objeto de renta
                val rentaRequest = RentaRequest(
                    persona = usuario,
                    vehiculo = vehiculo,
                    dias_renta = diasRenta,
                    valor_total_renta = vehiculo.valor_dia * diasRenta,
                    fecha_renta = fechaRenta,
                    fecha_entrega = fechaEntrega,
                    fecha_estimada_entrega = fechaEntrega
                )

                // Llamar a la API para registrar la renta
                RetrofitClient.vehiculosService.reservarVehiculo(apiKey, vehiculo.id, rentaRequest)

                // Remover el vehículo de la lista de disponibles
                vehiculos.removeIf { it.id == vehiculo.id }

                onResult(true, "Vehículo alquilado")

                // Actualizar la lista de vehículos rentados
                obtenerListadoVehiculos(apiKey, usuario.id)

            } catch (e: Exception) {
                onResult(false, "Error al alquilar vehículo")
            }
        }
    }

    /**
     * Entrega un vehículo rentado.
     * @param apiKey Clave de autenticación de la API.
     * @param vehiculoId Identificador del vehículo que se está devolviendo.
     * @param onResult Callback que indica el resultado de la operación.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun devolverVehiculo(apiKey: String, vehiculoId: Int, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                // Llamar a la API para marcar el vehículo como entregado
                RetrofitClient.vehiculosService.entregarVehiculo(apiKey, vehiculoId)

                // Actualizar la lista de rentas localmente
                val rentaIndex = rentas.indexOfFirst { it.vehiculo.id == vehiculoId }
                if (rentaIndex != -1) {
                    val rentaActualizada = rentas[rentaIndex].copy(fecha_entrega = obtenerFechaActual())
                    rentas[rentaIndex] = rentaActualizada
                }

                onResult(true, "Vehículo devuelto")

            } catch (e: Exception) {
                onResult(false, "Error al devolver el vehículo")
            }
        }
    }

    /**
     * Obtiene la fecha actual formateada.
     * @return String con la fecha en formato "yyyy-MM-dd HH:mm:ss".
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun obtenerFechaActual(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return LocalDateTime.now().format(formatter)
    }
}
