package com.pmd.rentavehiculos.Screen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pmd.rentavehiculos.model.Persona
import com.pmd.rentavehiculos.model.Vehiculo
import com.pmd.rentavehiculos.viewmodel.LoginViewModel
import com.pmd.rentavehiculos.viewmodel.VehiculosViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun VehiculosScreen(
    navController: NavController,
    vehiculosViewModel: VehiculosViewModel = viewModel(),
    loginViewModel: LoginViewModel = viewModel()
) {

    val apiKey = loginViewModel.apiKey.value
    val usuario = loginViewModel.usuario.value
    val vehiculos by vehiculosViewModel.vehiculosDisponibles.collectAsState()


    // 🔄 Obtener vehículos disponibles al cargar la pantalla
    LaunchedEffect(apiKey) {
        if (!apiKey.isNullOrEmpty()) {
            vehiculosViewModel.obtenerVehiculosDisponibles(apiKey)
        }
    }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Vehículos Disponibles") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (vehiculos.isEmpty()) {
            Text("No hay vehículos disponibles.", modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn {
                items(vehiculos) { vehiculo ->
                    VehiculoCard(vehiculo, apiKey, usuario, vehiculosViewModel)
                }
            }
        }

        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VehiculoCard(
    vehiculo: Vehiculo,
    apiKey: String?,
    usuario: Persona?,
    vehiculosViewModel: VehiculosViewModel
) {
    var diasRenta by remember { mutableStateOf("1") }
    val valorTotal = (diasRenta.toIntOrNull() ?: 1) * vehiculo.valor_dia

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            // 📋 Información del vehículo
            Text(
                text = "🚗 ${vehiculo.marca} - ${vehiculo.carroceria}",
                fontWeight = FontWeight.Bold
            )
            Text(text = "🔹 Color: ${vehiculo.color}")
            Text(text = "⚙️ Cambio: ${vehiculo.cambios}")
            Text(text = "⛽ Combustible: ${vehiculo.tipo_combustible}")
            Text(text = "💰 Precio por día: ${vehiculo.valor_dia}€")

            Spacer(modifier = Modifier.height(8.dp))

            // 📅 Selección de días de renta
            OutlinedTextField(
                value = diasRenta,
                onValueChange = { diasRenta = it.filter { char -> char.isDigit() } },
                label = { Text("Días de renta") },
                leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = "Días") },
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "💰 Total: $valorTotal€",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4CAF50),
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 🛒 Botón de Renta

            Button(
                onClick = {
                    Log.d("VehiculosScreen", "Botón alquilar presionado para ${vehiculo.marca}")

                    if (!apiKey.isNullOrEmpty() && usuario != null) {
                        vehiculosViewModel.reservarVehiculo(
                            apiKey,
                            usuario,
                            vehiculo,
                            3  // Puedes cambiar el número de días de alquiler
                        ) { success, message ->
                            Log.d("VehiculosScreen", "Resultado de la reserva: $message")
                        }
                    } else {
                        Log.e("VehiculosScreen", "❌ Error: API Key o Usuario es null")
                    }
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Alquilar")
            }
        }
    }
}
