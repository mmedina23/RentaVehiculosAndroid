package com.pmd.rentavehiculos.ui.theme.admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pmd.rentavehiculos.ui.theme.viewmodel.AdminViewModel

@Composable
fun HistorialRentasScreen(
    viewModel: AdminViewModel,
    vehiculoId: Int,
    navController: NavController
) {
    val rentas by viewModel.rentasLiveData.observeAsState(emptyList())

    // Llamar a la API cuando se carga la pantalla
    LaunchedEffect(Unit) {
        val apiKey = viewModel.obtenerToken()
        if (apiKey != null) {
            viewModel.obtenerHistorialRentas(apiKey, vehiculoId)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("📜 Historial de Rentas", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        if (rentas.isEmpty()) {
            Text("No hay historial de rentas para este vehículo.")
        } else {
            LazyColumn {
                items(rentas) { renta ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("📅 Fecha de Alquiler: ${renta.fechaRenta}")
                            Text("📆 Fecha Estimada de Entrega: ${renta.fechaPrevistaEntrega}")
                            Text("📅 Fecha de Entrega: ${renta.fechaEntrega ?: "No entregado"}")
                            Text("💰 Valor Total: $${renta.valorTotal}")
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text("Regresar")
        }
    }
}

