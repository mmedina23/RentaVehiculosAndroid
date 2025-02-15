package com.pmd.rentavehiculos.Screen

import android.util.Log
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehiculosRentadosScreen(
    navController: NavController,
    vehiculosViewModel: VehiculosViewModel = viewModel(),
    loginViewModel: LoginViewModel = viewModel()
) {
    val apiKey = loginViewModel.apiKey.value
    val personaId = loginViewModel.usuario.value?.id

    val rentas by vehiculosViewModel.rentas.collectAsState() // ✅ Usa collectAsState()

    LaunchedEffect(apiKey, personaId) {
        if (!apiKey.isNullOrEmpty() && personaId != null) {
            vehiculosViewModel.obtenerVehiculosRentados(apiKey, personaId)
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
            Text(
                text = "Historial de Rentas",
                style = MaterialTheme.typography.titleLarge
            )

            if (rentas.isEmpty()) { // ✅ Ahora funciona correctamente
                Text("No tienes vehículos rentados.")
            } else {
                LazyColumn {
                    items(rentas) { renta ->
                        RentaCard(renta)
                    }
                }
            }
        }
    }
}

/**
 * Tarjeta que muestra los datos de una renta sin opción de liberar.
 */

@Composable
fun RentaCard(renta: RentaRequest, apiKey: String?, vehiculosViewModel: VehiculosViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Vehículo: ${renta.vehiculo.marca}")
            Text(text = "Días rentados: ${renta.dias_renta}")
            Text(text = "Valor total: $${renta.valor_total_renta}")
            Text(text = "Fecha de renta: ${renta.fecha_renta}")
            Text(text = "Fecha estimada de entrega: ${renta.fecha_estimada_entrega}")

            // 🔥 Mostrar la fecha de entrega si ya fue devuelto
            if (!renta.fecha_estimada_entrega.isNullOrEmpty()) {
                Text(text = "Entregado: ${renta.fecha_estimada_entrega}")
            } else {
                Text(text = "No entregado aún")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ✅ Cliente puede liberar el vehículo si aún no lo ha entregado
            if (renta.fecha_estimada_entrega.isNullOrEmpty()) {
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
}
