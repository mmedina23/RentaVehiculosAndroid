package com.pmd.rentavehiculos.views

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.pmd.rentavehiculos.models.VehiculoRentado
import com.pmd.rentavehiculos.network.ApiClient
import com.pmd.rentavehiculos.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class VehiculosRentadosActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Obtener el token que se pasa desde el login
        val token = intent.getStringExtra("TOKEN") ?: ""

        setContent {
            VehiculosRentadosScreen(token)
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehiculosRentadosScreen(token: String) {
    val context = LocalContext.current
    var vehiculosRentados by remember { mutableStateOf<List<VehiculoRentado>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val apiService = ApiClient.retrofit.create(ApiService::class.java)

    LaunchedEffect(Unit) {
        val call = apiService.obtenerVehiculosRentados(token)
        call.enqueue(object : Callback<List<VehiculoRentado>> {
            override fun onResponse(call: Call<List<VehiculoRentado>>, response: Response<List<VehiculoRentado>>) {
                if (response.isSuccessful) {
                    vehiculosRentados = response.body() ?: emptyList()
                } else {
                    Toast.makeText(context, "Error al obtener vehículos", Toast.LENGTH_SHORT).show()
                }
                isLoading = false
            }

            override fun onFailure(call: Call<List<VehiculoRentado>>, t: Throwable) {
                Toast.makeText(context, "Error de conexión", Toast.LENGTH_SHORT).show()
                isLoading = false
            }
        })
    }

    Scaffold( // 🔴 Si Scaffold genera la advertencia
        topBar = { TopAppBar(title = { Text("Vehículos Rentados") }) } // 🔴 Si TopAppBar la genera
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            if (vehiculosRentados.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No has rentado ningún vehículo 😢")
                }
            } else {
                LazyColumn(modifier = Modifier.padding(padding)) {
                    items(vehiculosRentados) { vehiculo ->
                        VehiculoRentadoItem(vehiculo, token) {
                            vehiculosRentados = vehiculosRentados.filter { it.id != vehiculo.id }
                        }
                    }
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VehiculoRentadoItem(vehiculo: VehiculoRentado, token: String, onDevolverExitoso: () -> Unit) {
    val context = LocalContext.current
    val apiService = ApiClient.retrofit.create(ApiService::class.java)

    // 📌 Cálculo de días rentados
    val diasRenta = calcularDiasRenta(vehiculo.fechaRenta, vehiculo.fechaEntrega)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "${vehiculo.marca} ${vehiculo.modelo}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Días rentados: $diasRenta", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Total: \$${vehiculo.precioTotal}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun calcularDiasRenta(fechaRenta: String, fechaEntrega: String?): Long {
    val formato = DateTimeFormatter.ofPattern("yyyy-MM-dd") // Asegúrate que coincide con la API
    val fechaInicio = LocalDate.parse(fechaRenta, formato)
    val fechaFin = fechaEntrega?.let { LocalDate.parse(it, formato) } ?: LocalDate.now()

    return ChronoUnit.DAYS.between(fechaInicio, fechaFin)
}
