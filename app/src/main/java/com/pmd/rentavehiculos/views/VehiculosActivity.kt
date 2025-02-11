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

        val token = intent.getStringExtra("TOKEN") ?: ""

        setContent {
            VehiculosScreen(token)
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
        println("📢 Cargando vehículos con token: $token")

        val call = apiService.obtenerVehiculos(token)
        call.enqueue(object : Callback<List<Vehiculo>> {
            override fun onResponse(call: Call<List<Vehiculo>>, response: Response<List<Vehiculo>>) {
                if (response.isSuccessful) {
                    vehiculos = response.body() ?: emptyList()
                    println("✅ Vehículos obtenidos: $vehiculos")
                } else {
                    println("❌ Error al obtener vehículos: ${response.errorBody()?.string()}")
                    Toast.makeText(context, "Error al obtener vehículos", Toast.LENGTH_SHORT).show()
                }
                isLoading = false
            }

            override fun onFailure(call: Call<List<Vehiculo>>, t: Throwable) {
                println("❌ Error de conexión: ${t.message}")
                Toast.makeText(context, "Error de conexión", Toast.LENGTH_SHORT).show()
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
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(vehiculos) { vehiculo ->
                    VehiculoItem(vehiculo, token)
                }
            }
        }
    }
}

@Composable
fun VehiculoItem(vehiculo: Vehiculo, token: String) {
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
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "${vehiculo.marca} - ${vehiculo.carroceria}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Color: ${vehiculo.color}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Precio: \$${vehiculo.valorDia}/día", style = MaterialTheme.typography.bodyMedium)
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
                        val identificacionPersona = "2354789" // 🔥 Cambia esto por la identificación real del usuario
                        rentarVehiculo(apiService, token, vehiculo.id, identificacionPersona, diasInt, context) {
                            showDialog = false
                        }
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


fun rentarVehiculo(
    apiService: ApiService,
    token: String,
    vehiculoId: Int,
    identificacionPersona: String, // Se pasa solo la identificación de la persona
    dias: Int, // ✅ Ahora es un Int
    context: Context,
    onRentarExitoso: () -> Unit
) {
    val request = mapOf(
        "persona" to mapOf("identificacion" to identificacionPersona),
        "dias_renta" to dias
    )

    println("📢 Enviando petición de renta: Vehículo ID: $vehiculoId, Días: $dias, Token: $token")
    println("📢 Cuerpo de la petición: $request")

    val call = apiService.rentarVehiculo(token, vehiculoId, request)
    call.enqueue(object : Callback<RentarVehiculoResponse> {
        override fun onResponse(call: Call<RentarVehiculoResponse>, response: Response<RentarVehiculoResponse>) {
            println("📢 Respuesta del servidor: Código ${response.code()} - ${response.message()}")

            if (response.isSuccessful) {
                Toast.makeText(context, "✅ Vehículo rentado con éxito", Toast.LENGTH_SHORT).show()
                onRentarExitoso()
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Error desconocido"
                println("❌ Error al rentar: $errorMsg")
                Toast.makeText(context, "❌ Error al rentar: $errorMsg", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<RentarVehiculoResponse>, t: Throwable) {
            println("❌ Error de conexión: ${t.message}")
            Toast.makeText(context, "❌ Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
        }
    })
}
