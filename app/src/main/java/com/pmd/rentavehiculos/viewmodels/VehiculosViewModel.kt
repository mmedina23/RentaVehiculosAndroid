package com.pmd.rentavehiculos.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pmd.rentavehiculos.modelos.Persona
import com.pmd.rentavehiculos.modelos.Renta
import com.pmd.rentavehiculos.modelos.Vehiculo
import com.pmd.rentavehiculos.retrofit.ApiService
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class VehiculosViewModel(
    private val apiService: ApiService
) : ViewModel() {

    class Factory(private val apiService: ApiService) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(VehiculosViewModel::class.java)) {
                return VehiculosViewModel(apiService) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    private var apiKey: String = ""
    private var personaId: Int = -1

    private val _vehiculosDisponibles = MutableLiveData<List<Vehiculo>>()
    val vehiculosDisponibles: LiveData<List<Vehiculo>> = _vehiculosDisponibles

    private val _vehiculosRentados = MutableLiveData<List<Renta>>()
    val vehiculosRentados: LiveData<List<Renta>> = _vehiculosRentados

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun setCredentials(apiKey: String, personaId: Int) {
        this.apiKey = apiKey
        this.personaId = personaId
        Log.d("API_DEBUG", "API Key establecida: $apiKey, Persona ID: $personaId")
    }

    fun fetchVehiculosDisponibles() {
        viewModelScope.launch {
            try {
                _vehiculosDisponibles.postValue(emptyList()) // Limpia la caché antes de actualizar
                val vehiculos = apiService.getVehiculos(apiKey)

                vehiculos.forEach { vehiculo ->
                    Log.d("API_DEBUG", "Vehículo recibido - ID: ${vehiculo.id}, Marca: ${vehiculo.marca}, Disponible: ${vehiculo.disponible}")
                }

                _vehiculosDisponibles.postValue(vehiculos.filter { it.disponible })
            } catch (e: Exception) {
                _errorMessage.postValue("Error al obtener vehículos: ${e.message}")
                Log.e("API_DEBUG", "Error en fetchVehiculosDisponibles: ${e.message}")
            }
        }
    }


    fun rentarVehiculo(
        vehiculo: Vehiculo,
        persona: Persona,
        diasRenta: Int,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (apiKey.isEmpty()) {
                    onResult(false, "Error: apiKey está vacía")
                    Log.e("API_DEBUG", "Error: apiKey está vacía")
                    return@launch
                }

                if (personaId == -1) {
                    onResult(false, "Error: personaId inválido")
                    Log.e("API_DEBUG", "Error: personaId inválido")
                    return@launch
                }

                // 🔥 Verificar si el usuario ya tiene 3 rentas activas
                val rentasActuales = apiService.getRentasByPersona(apiKey, personaId)
                if (rentasActuales.size >= 3) {
                    Log.w("API_DEBUG", "Usuario ya tiene 3 vehículos rentados, no puede rentar más.")
                    onResult(false, "No puedes rentar más de 3 vehículos a la vez.")
                    return@launch
                }

                Log.d("API_DEBUG", "Intentando rentar vehículo con ID: ${vehiculo.id}, Persona ID: $personaId")

                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val calendar = Calendar.getInstance()
                val fechaRenta = dateFormat.format(calendar.time)

                calendar.add(Calendar.DAY_OF_YEAR, diasRenta)
                val fechaEntrega = dateFormat.format(calendar.time)

                val renta = Renta(
                    persona = persona,
                    vehiculo = vehiculo,
                    dias_renta = diasRenta,
                    valor_total_renta = vehiculo.valor_dia * diasRenta,
                    fecha_renta = fechaRenta,
                    fecha_estimada_entrega = fechaEntrega
                )

                val response = apiService.rentarVehiculo(apiKey, vehiculo.id, renta)

                if (response.isSuccessful) {
                    Log.d("API_DEBUG", "Vehículo rentado correctamente: ${vehiculo.marca}")

                    // esperar 1 seg para actualizar la lista de vehículos disponibles
                    kotlinx.coroutines.delay(1000)

                    fetchVehiculosRentados()  //Actualiza la lista de vehículos rentados
                    fetchVehiculosDisponibles()  //Actualiza la lista de vehículos disponibles

                    onResult(true, "Reserva exitosa")
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Error desconocido"
                    Log.e("API_DEBUG", "Error al rentar vehículo: HTTP ${response.code()} - $errorMsg")
                    onResult(false, "Error en la API: $errorMsg")
                }
            } catch (e: Exception) {
                Log.e("API_DEBUG", "Error en rentarVehiculo: ${e.message}", e)
                onResult(false, "Error al rentar vehículo: ${e.message}")
            }
        }
    }




    fun liberarVehiculo(vehiculoId: Int, onResult: (Boolean, String) -> Unit) {
        Log.d("API_DEBUG", "Intentando liberar vehículo con API Key: $apiKey, Vehiculo ID: $vehiculoId")

        viewModelScope.launch {
            try {
                if (apiKey.isEmpty()) {
                    Log.e("API_DEBUG", "Error: API Key vacía")
                    onResult(false, "Error: apiKey está vacía")
                    return@launch
                }



                val response = apiService.liberarVehiculo(apiKey, vehiculoId)


                if (response.isSuccessful) {
                    Log.d("API_DEBUG", "Vehículo liberado correctamente")
                    kotlinx.coroutines.delay(1000)
                    fetchVehiculosRentados()
                    fetchVehiculosDisponibles()
                    onResult(true, "Vehículo liberado con éxito")
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Error desconocido"
                    Log.e("API_DEBUG", "Error al liberar vehículo: HTTP ${response.code()} - $errorMsg")
                    onResult(false, "Error al liberar vehículo: HTTP ${response.code()} - $errorMsg")
                }
            } catch (e: Exception) {
                Log.e("API_DEBUG", "Error al liberar vehículo: ${e.message}")
                onResult(false, "Error al liberar vehículo: ${e.message}")
            }
        }
    }







    fun fetchVehiculosRentados() {//PARA CLIENTE
        viewModelScope.launch {
            try {
                _vehiculosRentados.postValue(emptyList()) // Limpia la caché antes de actualizar
                val rentasObtenidas = apiService.getRentasByPersona(apiKey, personaId)

                rentasObtenidas.forEach { renta ->
                    Log.d("API_DEBUG", "Vehículo rentado: ${renta.vehiculo.marca}, Disponible: ${renta.vehiculo.disponible}")
                }

                _vehiculosRentados.postValue(rentasObtenidas)
            } catch (e: Exception) {
                _vehiculosRentados.postValue(emptyList())
                _errorMessage.postValue("Error al obtener rentas: ${e.message}")
                Log.e("API_DEBUG", "Error en fetchVehiculosRentados: ${e.message}")
            }
        }
    }


    fun fetchVehiculosRentadosAdmin() {
        viewModelScope.launch {
            try {
                Log.d("API_DEBUG", "Ejecutando fetchVehiculosRentadosAdmin()")

                _vehiculosRentados.postValue(emptyList()) // 🔥 Limpia antes de actualizar

                val vehiculos = apiService.getVehiculos(apiKey) // ✅ Obtener todos los vehículos
                Log.d("API_DEBUG", "Vehículos obtenidos: ${vehiculos.size}")

                val todasLasRentas = mutableListOf<Renta>()

                for (vehiculo in vehiculos) {
                    Log.d("API_DEBUG", "Obteniendo historial de rentas para vehículo ID: ${vehiculo.id}")

                    try {
                        val rentas = apiService.getRentasByVehiculo(apiKey, vehiculo.id) // ✅ Usa el historial de rentas
                        Log.d("API_DEBUG", "Rentas obtenidas para vehículo ${vehiculo.id}: ${rentas.size}")

                        todasLasRentas.addAll(rentas)
                    } catch (e: Exception) {
                        Log.e("API_DEBUG", "Error al obtener rentas del vehículo ${vehiculo.id}: ${e.message}")
                    }
                }

                _vehiculosRentados.postValue(todasLasRentas) // ✅ Actualiza LiveData con todas las rentas
                Log.d("API_DEBUG", "Total de rentas obtenidas: ${todasLasRentas.size}")

            } catch (e: Exception) {
                _vehiculosRentados.postValue(emptyList())
                _errorMessage.postValue("Error al obtener rentas de todos los vehículos: ${e.message}")
                Log.e("API_DEBUG", "Error en fetchVehiculosRentadosAdmin: ${e.message}")
            }
        }
    }







}