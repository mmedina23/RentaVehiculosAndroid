package com.pmd.rentavehiculos.views

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.pmd.rentavehiculos.models.Persona
import com.pmd.rentavehiculos.models.PersonaRequest
import com.pmd.rentavehiculos.models.RentarVehiculoRequest
import com.pmd.rentavehiculos.models.RentarVehiculoResponse
import com.pmd.rentavehiculos.models.Vehiculo
import com.pmd.rentavehiculos.models.VehiculoRequest
import com.pmd.rentavehiculos.network.ApiClient
import com.pmd.rentavehiculos.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date

class VehiculosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val token = intent.getStringExtra("TOKEN") ?: ""
        val personaId = intent.getIntExtra("PERSONA_ID", 0) // 🔹 Asegurar que se recibe correctamente

        setContent {
            VehiculosScreen(token, personaId)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehiculosScreen(token: String, personaId: Int) {
    val context = LocalContext.current
    var vehiculos by remember { mutableStateOf<List<Vehiculo>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val apiService = ApiClient.retrofit.create(ApiService::class.java)

    fun cargarVehiculos() {
        isLoading = true
        val call = apiService.obtenerVehiculos(token)
        call.enqueue(object : Callback<List<Vehiculo>> {
            override fun onResponse(call: Call<List<Vehiculo>>, response: Response<List<Vehiculo>>) {
                if (response.isSuccessful) {
                    vehiculos = response.body() ?: emptyList()
                } else {
                    vehiculos = emptyList()
                    Log.e("VehiculosScreen", "Error al obtener vehículos: ${response.errorBody()?.string()}")
                    Toast.makeText(context, "Error al obtener vehículos", Toast.LENGTH_SHORT).show()
                }
                isLoading = false
            }

            override fun onFailure(call: Call<List<Vehiculo>>, t: Throwable) {
                vehiculos = emptyList()
                Log.e("VehiculosScreen", "Error de conexión: ${t.message}")
                Toast.makeText(context, "Error de conexión", Toast.LENGTH_SHORT).show()
                isLoading = false
            }
        })
    }

    LaunchedEffect(Unit) {
        cargarVehiculos()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Vehículos Disponibles") }) }
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (vehiculos.isNullOrEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No hay vehículos disponibles 😢")
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(vehiculos!!) { vehiculo ->
                    VehiculoItem(
                        vehiculo, token, personaId,
                        onRentarSuccess = { cargarVehiculos() }
                    )
                }
            }
        }
    }
}

@Composable
fun VehiculoItem(vehiculo: Vehiculo, token: String, personaId: Int, onRentarSuccess: () -> Unit) {
    val context = LocalContext.current
    val apiService = ApiClient.retrofit.create(ApiService::class.java)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "${vehiculo.marca} - ${vehiculo.carroceria}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Color: ${vehiculo.color}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Precio: \$${vehiculo.valorDia}/día", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                val persona = Persona(
                    id = personaId, // 🔹 Se obtiene desde el intent
                    nombre = "Juan",
                    apellidos = "Pérez",
                    direccion = "Calle 123, Madrid",
                    telefono = "123456789",
                    tipoIdentificacion = "DNI",
                    identificacion = "12345678A"
                )

                rentarVehiculo(apiService, token, persona, vehiculo, diasRenta = 3) { success, errorMessage ->
                    if (success) {
                        Toast.makeText(context, "🚗 Vehículo rentado con éxito", Toast.LENGTH_SHORT).show()
                        onRentarSuccess()
                    } else {
                        Toast.makeText(context, "❌ Error al rentar vehículo: $errorMessage", Toast.LENGTH_LONG).show()
                    }
                }
            }) {
                Text("Rentar")
            }


        }
    }
}

fun rentarVehiculo(
    apiService: ApiService,
    token: String,
    persona: Persona, // 🔹 Aquí pasamos `Persona` en lugar de `PersonaRequest`
    vehiculo: Vehiculo,
    diasRenta: Int,
    onResult: (Boolean, String?) -> Unit
) {
    val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", java.util.Locale.getDefault())
    val fechaRenta = dateFormat.format(java.util.Date())

    val calendar = java.util.Calendar.getInstance()
    calendar.add(java.util.Calendar.DAY_OF_YEAR, diasRenta)
    val fechaEntrega = dateFormat.format(calendar.time)

    // 🔹 Si la API espera un ID en lugar del objeto `Persona`, enviamos `personaId` en el JSON.
    val requestBody = RentarVehiculoRequest(
        personaId = persona.id,  // Si la API solo espera el ID, enviamos solo esto
        vehiculoId = vehiculo.id, // 🔹 Si la API solo espera el ID del vehículo
        dias_renta = diasRenta,
        valor_total_renta = vehiculo.valorDia * diasRenta,
        fecha_renta = fechaRenta,
        fecha_estimada_entrega = fechaEntrega
    )

    Log.d("RentarVehiculo", "📤 Enviando datos al servidor:")
    Log.d("RentarVehiculo", "🔹 Token: $token")
    Log.d("RentarVehiculo", "🔹 JSON: $requestBody")

    val call = apiService.rentarVehiculo(token, vehiculo.id, requestBody)
    call.enqueue(object : Callback<RentarVehiculoResponse> {
        override fun onResponse(call: Call<RentarVehiculoResponse>, response: Response<RentarVehiculoResponse>) {
            if (response.isSuccessful) {
                Log.d("RentarVehiculo", "✅ Renta exitosa para vehículo ID: ${vehiculo.id}")
                onResult(true, null)
            } else {
                val error = "Error HTTP: ${response.code()} - ${response.message()}"
                Log.e("RentarVehiculo", "❌ Error al rentar vehículo: $error")
                Log.e("RentarVehiculo", "🔍 Respuesta del servidor: ${response.errorBody()?.string()}")
                onResult(false, error)
            }
        }

        override fun onFailure(call: Call<RentarVehiculoResponse>, t: Throwable) {
            Log.e("RentarVehiculo", "❌ Error de conexión: ${t.message}")
            onResult(false, "Error de conexión: ${t.message}")
        }
    })
}


