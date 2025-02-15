package com.pmd.rentavehiculos.Screen

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
fun VehiculosRentadosAdminScreen(
    navController: NavController,
    vehiculoId: Int,
    vehiculosViewModel: VehiculosViewModel = viewModel(),
    loginViewModel: LoginViewModel = viewModel()
) {
    val apiKey = loginViewModel.apiKey.value
    val rentas by vehiculosViewModel.rentas.collectAsState() // ✅ Usa collectAsState()

    LaunchedEffect(apiKey, vehiculoId) {
        if (!apiKey.isNullOrEmpty()) {
            vehiculosViewModel.obtenerVehiculosRentadosAdmin(apiKey, vehiculoId)
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
                text = "Historial del vehículo ID: $vehiculoId",
                style = MaterialTheme.typography.titleLarge
            )

            if (rentas.isEmpty()) { // ✅ Ahora rentas puede usar isEmpty()
                Text("No hay historial de rentas para este vehículo.")
            } else {
                LazyColumn {
                    items(rentas) { renta ->
                        RentaCardAdmin(renta)
                    }
                }
            }
        }
    }
}

@Composable
fun RentaCardAdmin(renta: RentaRequest) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Cliente: ${renta.persona.nombre} ${renta.persona.apellidos}")
            Text(text = "Días rentados: ${renta.dias_renta}")
            Text(text = "Valor total: $${renta.valor_total_renta}")
            Text(text = "Fecha de renta: ${renta.fecha_renta}")
            Text(text = "Fecha estimada de entrega: ${renta.fecha_estimada_entrega}")
            Text(text = if (!renta.fecha_estimada_entrega.isNullOrEmpty()) "Entregado: ${renta.fecha_estimada_entrega}" else "No entregado aún")
        }
    }
}
