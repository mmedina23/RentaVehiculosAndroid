package com.pmd.rentavehiculos.viewModels


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmd.rentavehiculos.data.RetrofitClient
import com.pmd.rentavehiculos.modelo.Persona
import com.pmd.rentavehiculos.modelo.RentaVehiculo
import com.pmd.rentavehiculos.modelo.RentarVehiculoRequest
import com.pmd.rentavehiculos.modelo.Vehiculo
import com.pmd.rentavehiculos.repositorio.RentaRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response

class VehiculosViewModel : ViewModel() {

    private val repository = RentaRepository()

    // LiveData para la lista de vehículos disponibles
    val vehiculosDisponiblesLiveData = MutableLiveData<List<Vehiculo>>()
    // LiveData para la lista de vehículos rentados
    val vehiculosRentadosLiveData = MutableLiveData<List<RentaVehiculo>>()
    // LiveData para el detalle de un vehículo
    val vehiculoDetalleLiveData = MutableLiveData<Vehiculo?>()
    // LiveData para errores
    val errorLiveData = MutableLiveData<String>()

    // Función para obtener vehículos disponibles
    fun obtenerVehiculosDisponibles(apiKey: String) {
        viewModelScope.launch {
            try {
                val vehiculos = repository.obtenerVehiculosDisponibles(apiKey)
                vehiculosDisponiblesLiveData.value = vehiculos
            } catch (ex: Exception) {
                errorLiveData.value = "Error al obtener vehículos: ${ex.message}"
            }
        }
    }

    // Función para rentar un vehículo
    fun rentarVehiculo(
        apiKey: String,
        usuario: Persona,  // Se requiere para extraer el id y poder hacer la validación previa
        vehiculoId: Int,
        request: RentarVehiculoRequest,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Llamar al repositorio para obtener la lista ya desempaquetada
                val rentasActualizadas = repository.obtenerVehiculosRentados(apiKey, usuario.id)

                // Ahora rentasActualizadas es de tipo List<RentaVehiculo> y se puede filtrar
                val vehiculosRentados = rentasActualizadas.filter { it.fechaEntrega.isNullOrEmpty() }

                if (vehiculosRentados.size >= 3) {
                    onError("No puedes rentar más de 3 vehículos a la vez.")
                    return@launch
                }

                // Realiza la reserva enviando la solicitud con la estructura mínima
                repository.reservarVehiculo(apiKey, vehiculoId, request)
                onSuccess()
            } catch (ex: Exception) {
                val mensajeError = "Error al rentar el vehículo: ${ex.message}"
                errorLiveData.value = mensajeError
                Log.e("VIEWMODEL", mensajeError, ex)
                onError(mensajeError)
            }
        }
    }

    // Función para obtener vehículos rentados
//    fun obtenerVehiculosRentados(apiKey: String, personaId: Int) {
//        viewModelScope.launch {
//            try {
//                val rentas = repository.obtenerVehiculosRentados(apiKey, personaId)
//                vehiculosRentadosLiveData.value = rentas
//            } catch (ex: Exception) {
//                errorLiveData.value = "Error al obtener vehículos rentados: ${ex.message}"
//            }
//        }
//    }
//    /**
//     * Obtiene los vehículos rentados por un usuario.
//     */
//    fun obtenerVehiculosRentados(apiKey: String, personaId: Int) {
//        viewModelScope.launch {
//            try {
//                val rentasObtenidas = RetrofitClient.vehiculosService.obtenerVehiculosRentados(apiKey, personaId)
//                rentas.clear()
//                rentas.addAll(rentasObtenidas)
//            } catch (e: Exception) {
//                rentas.clear()
//            }
//        }
//    }

    // Función para entregar un vehículo
    fun entregarVehiculo(apiKey: String, vehiculoId: Int) {
        viewModelScope.launch {
            try {
                repository.entregarVehiculo(apiKey, vehiculoId)
                // Puedes actualizar la lista luego de entregar, si fuera necesario
            } catch (ex: Exception) {
                errorLiveData.value = "Error al entregar vehículo: ${ex.message}"
            }
        }
    }

    // Función para obtener el detalle de un vehículo
    fun obtenerDetalleVehiculo(apiKey: String, vehiculoId: Int) {
        viewModelScope.launch {
            try {
                val vehiculo = repository.obtenerDetalleVehiculo(apiKey, vehiculoId)
                Log.d("VIEWMODEL", "Detalle obtenido: ${vehiculo.id}")
                vehiculoDetalleLiveData.value = vehiculo
            } catch (ex: Exception) {
                Log.e("VIEWMODEL", "Error en obtenerDetalleVehiculo", ex)
                errorLiveData.value = "Error al obtener detalle: ${ex.message}"
            }
        }
    }
}