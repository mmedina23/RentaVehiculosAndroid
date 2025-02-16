package com.pmd.rentavehiculos.Screen

import android.content.Context
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
fun VehiculosRentadosScreen(
    navController: NavController,
    vehiculosViewModel: VehiculosViewModel = viewModel(),
    loginViewModel: LoginViewModel = viewModel(),
    context: Context // ✅ Se añade el parámetro context
) {
    val apiKey = loginViewModel.apiKey.value
    val personaId = loginViewModel.usuario.value?.id

    val rentas by vehiculosViewModel.rentas.collectAsState()

    LaunchedEffect(apiKey, personaId) {
        if (!apiKey.isNullOrEmpty() && personaId != null) {
            Log.d("VehiculosRentadosScreen", "🔄 Cargando rentas...")
            vehiculosViewModel.obtenerVehiculosRentados(apiKey, personaId, context) // ✅ Pasa los 3 parámetros


            // ✅ Programar notificación si hay rentas con entrega próxima
            rentas.forEach { renta ->
                renta.fecha_estimada_entrega?.let { fechaEntrega ->
                    vehiculosViewModel.programarNotificacion(context, renta.vehiculo.marca, fechaEntrega)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Mis Vehículos Rentados") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(text = "Historial de Rentas", style = MaterialTheme.typography.titleLarge)

            if (rentas.isEmpty()) {
                Text("No tienes vehículos rentados.")
            } else {
                LazyColumn {
                    items(rentas) { renta ->
                        RentaCard(renta, apiKey, vehiculosViewModel)
                    }
                }
            }
        }
    }
}



/**
 * Tarjeta que muestra los datos de una renta sin opción de liberar.
 */

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentaCard(renta: RentaRequest, apiKey: String?, vehiculosViewModel: VehiculosViewModel) {
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

            Button(
                onClick = {
                    if (!apiKey.isNullOrEmpty()) {
                        vehiculosViewModel.liberarVehiculo(apiKey, renta.vehiculo.id) { success, message ->
                            Log.d("VehiculosRentadosScreen", "Liberación: $message")
                        }
                    }
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Liberar Vehículo")
            }
        }
    }
}




