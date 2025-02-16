package com.pmd.rentavehiculos.Screen

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pmd.rentavehiculos.model.RentaRequest
import com.pmd.rentavehiculos.viewmodel.LoginViewModel
import com.pmd.rentavehiculos.viewmodel.VehiculosViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehiculosRentadosAdminScreen(
    navController: NavController,
    vehiculoId: Int,
    vehiculosViewModel: VehiculosViewModel = viewModel(),
    loginViewModel: LoginViewModel = viewModel()
) {
    val apiKey = loginViewModel.apiKey.value

    val rentas by vehiculosViewModel.rentas.collectAsState()

    LaunchedEffect(apiKey, vehiculoId) {
        if (!apiKey.isNullOrEmpty() && vehiculoId != 0) {
            vehiculosViewModel.obtenerHistorialRentasAdmin(apiKey, vehiculoId) // ✅ Ahora se pasa vehiculoId
        }
    }



    if (rentas.isEmpty()) {
        Text("No hay rentas registradas.")
    } else {
        LazyColumn {
            items(rentas) { renta ->
                RentaCardAdmin(renta, apiKey, vehiculosViewModel)
            }
        }
    }



    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Historial de Rentas") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Rentas de Clientes",
                style = MaterialTheme.typography.titleLarge
            )

            if (rentas.isEmpty()) {
                Text("No hay rentas registradas.")
            } else {
                LazyColumn {
                    items(rentas) { renta ->
                        RentaCardAdmin(renta, apiKey, vehiculosViewModel)
                    }
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("LongLogTag")
@Composable
fun RentaCardAdmin(renta: RentaRequest, apiKey: String?, vehiculosViewModel: VehiculosViewModel) {
    Log.d("RentaCardAdmin", "🎴 Mostrando renta: ${renta.vehiculo.marca}")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Cliente: ${renta.persona.nombre} ${renta.persona.apellidos}")
            Text(text = "Vehículo: ${renta.vehiculo.marca}")
            Text(text = "Días rentados: ${renta.dias_renta}")
            Text(text = "Fecha de renta: ${renta.fecha_renta}")
            Text(text = "Fecha estimada de entrega: ${renta.fecha_estimada_entrega}")

            if (!renta.fecha_estimada_entrega.isNullOrEmpty()) {
                Text(text = "Entregado: ${renta.fecha_estimada_entrega}")
            } else {
                Text(text = "No entregado aún")
            }
        }
    }
            Spacer(modifier = Modifier.height(8.dp))

            // ✅ Administrador puede liberar el vehículo rentado
            Button(
                onClick = {
                    if (!apiKey.isNullOrEmpty() && renta.vehiculo.id != null) {
                        vehiculosViewModel.liberarVehiculo(apiKey, renta.vehiculo.id) { success, message ->
                            Log.d("VehiculosRentadosScreen", "Liberación: $message")
                        }
                    } else {
                        Log.e("VehiculosRentadosScreen", "❌ Error: API Key o ID del vehículo es nulo")
                    }

                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Liberar Vehículo")
            }
        }




