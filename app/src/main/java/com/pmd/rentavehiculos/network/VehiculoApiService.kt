package com.pmd.rentavehiculos.network
//IMPORTANTE: EN EL RENTVEHICULOSAPI EJECUTA EL PROGRAMA y LUEGO ESCRIBE EN EL NAVEGADOR: http://localhost:8085/api/v1
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Query

// Modelo de datos (esto se ajustará después según la API)
data class Vehiculo( //data => estructura que representa un vehículo (esto se puede modificar más
// adelante según la API real).
    val id: Int,
    val marca: String,
    val modelo: String,
    val disponible: Boolean,
    val precioPorDia: Double
)

// Interfaz para comunicarnos con la API
interface VehiculoApiService {
    @GET("vehiculos") // Endpoint para obtener la lista de vehículos. Le dice a Retrofit que este
    // metodo llamara al endpoint https://api.renta-vehiculos.com/vehiculos.
    suspend fun obtenerVehiculos(
        @Header("x-llave-api") apiKey: String, // Header requerido
        @Query("estado") estado: String? = null // Parámetro opcional
    ): List<Vehiculo>
    }