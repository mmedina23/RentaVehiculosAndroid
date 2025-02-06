package com.pmd.rentavehiculos.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pmd.rentavehiculos.viewmodel.VehiculoViewModel

@Composable
fun VehiculoScreen(llaveApi: String) {
    val viewModel: VehiculoViewModel = viewModel()
    val listaVehiculos by viewModel.listaVehiculos.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.obtenerVehiculos(llaveApi)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Lista de Vehículos", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))

        listaVehiculos.forEach { vehiculo ->
            Text(text = "🚗 Marca: ${vehiculo.marca}, Carrocería: ${vehiculo.carroceria}")
        }
    }
}
