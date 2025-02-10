package com.pmd.rentavehiculos.views

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.pmd.rentavehiculos.models.RentarVehiculoRequest
import com.pmd.rentavehiculos.models.RentarVehiculoResponse
import com.pmd.rentavehiculos.models.Vehiculo
import com.pmd.rentavehiculos.network.ApiClient
import com.pmd.rentavehiculos.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VehiculosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val token = intent.getStringExtra("TOKEN") ?: ""  // 📌 Recibir el token del Intent

        setContent {
            VehiculosScreen(token)  // 📌 Pasamos el token correctamente
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehiculosScreen(token: String) {
    val context = LocalContext.current
    var vehiculos by remember { mutableStateOf<List<Vehiculo>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val apiService = ApiClient.retrofit.create(ApiService::class.java)

    LaunchedEffect(Unit) {
        println("📢 Cargando lista de vehículos con token: $token") // Log de depuración

        val call = apiService.obtenerVehiculos(token)
        call.enqueue(object : Callback<List<Vehiculo>> {
            override fun onResponse(call: Call<List<Vehiculo>>, response: Response<List<Vehiculo>>) {
                if (response.isSuccessful) {
                    vehiculos = response.body() ?: emptyList()
                    println("✅ Vehículos obtenidos: $vehiculos") // Log de éxito
                } else {
                    println("❌ Error al obtener vehículos: ${response.errorBody()?.string()}")
                    Toast.makeText(context, "Error al obtener vehículos", Toast.LENGTH_SHORT).show()
                }
                isLoading = false
            }

            override fun onFailure(call: Call<List<Vehiculo>>, t: Throwable) {
                println("❌ Error de conexión: ${t.message}")
                Toast.makeText(context, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                isLoading = false
            }
        })
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Vehículos Disponibles") }) }
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            if (vehiculos.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay vehículos disponibles 😢")
                }
            } else {
                LazyColumn(modifier = Modifier.padding(padding)) {
                    items(vehiculos) { vehiculo ->
                        VehiculoItem(vehiculo, token) {
                            vehiculos = vehiculos.filter { it.id != vehiculo.id }
                        }
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehiculoItem(vehiculo: Vehiculo, token: String, onRentarExitoso: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    var dias by remember { mutableStateOf("") }
    val context = LocalContext.current
    val apiService = ApiClient.retrofit.create(ApiService::class.java)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { showDialog = true },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column {
                Text(text = "${vehiculo.marca} ${vehiculo.modelo}", style = MaterialTheme.typography.titleMedium)
                Text(text = "Precio: \$${vehiculo.valorDia}/día", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Rentar Vehículo") },
            text = {
                Column {
                    Text("Precio por día: \$${vehiculo.valorDia}")
                    OutlinedTextField(
                        value = dias,
                        onValueChange = { dias = it },
                        label = { Text("Días de renta") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    if (dias.isNotEmpty()) {
                        val diasInt = dias.toIntOrNull() ?: 0
                        val total = diasInt * vehiculo.valorDia
                        Text("Total a pagar: \$${total}")
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    val diasInt = dias.toIntOrNull()
                    if (diasInt != null && diasInt > 0) {
                        rentarVehiculo(apiService, token, vehiculo.id, diasInt, context, onRentarExitoso)
                        showDialog = false
                    } else {
                        Toast.makeText(context, "Ingrese una cantidad válida de días", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

// ✅ Función separada para rentar el vehículo correctamente
fun rentarVehiculo(
    apiService: ApiService,
    token: String,
    vehiculoId: Int,
    dias: Int,
    context: Context,
    onRentarExitoso: () -> Unit
) {
    val request = RentarVehiculoRequest(vehiculoId, dias)

    println("📢 Enviando petición de renta: Vehículo ID: $vehiculoId, Días: $dias, Token: $token")

    val call = apiService.rentarVehiculo(token, request)
    call.enqueue(object : Callback<RentarVehiculoResponse> {
        override fun onResponse(call: Call<RentarVehiculoResponse>, response: Response<RentarVehiculoResponse>) {
            println("📢 Respuesta del servidor: ${response.code()} - ${response.message()}")

            if (response.isSuccessful) {
                Toast.makeText(context, "Vehículo rentado con éxito", Toast.LENGTH_SHORT).show()
                onRentarExitoso()
            } else {
                Toast.makeText(context, "Error al rentar: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<RentarVehiculoResponse>, t: Throwable) {
            println("❌ Error de conexión: ${t.message}")
            Toast.makeText(context, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
        }
    })
}
