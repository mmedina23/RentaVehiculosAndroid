package com.pmd.rentavehiculos.ui.theme.admin

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pmd.rentavehiculos.data.model.Vehiculo
import com.pmd.rentavehiculos.ui.theme.viewmodel.AdminViewModel
@Composable
fun ListaVehiculosDisponibles(navController: NavController, context: Context) {
    val viewModel: AdminViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return AdminViewModel(context) as T
            }
        }
    )

    LaunchedEffect(Unit) {
        viewModel.loadVehiculosDisponibles()
    }

    val vehiculosDisponibles by viewModel.vehiculosDisponibles.observeAsState(emptyList())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("🚘 Vehículos Disponibles", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        if (vehiculosDisponibles.isEmpty()) {
            Text("No hay vehículos disponibles", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(vehiculosDisponibles) { vehiculo ->
                    VehiculoDisponibleCard(vehiculo) { vehiculoId ->
                        navController.navigate("historial_rentas/$vehiculoId")
                    }
                    Divider(thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
                }
            }
        }
    }
}


@Composable
fun VehiculoDisponibleCard(vehiculo: Vehiculo, onVerHistorialClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            AsyncImage(
                model = vehiculo.imagen,
                contentDescription = "Imagen del vehículo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Marca: ${vehiculo.marca}", style = MaterialTheme.typography.bodyLarge)
            Text("Modelo: ${vehiculo.carroceria}")
            Text("Color: ${vehiculo.color}")
            Text("Plazas: ${vehiculo.plazas}")
            Text("Tipo Combustible: ${vehiculo.tipo_combustible}")
            Text("Valor por Día: $${vehiculo.valor_dia}")

            Spacer(modifier = Modifier.height(8.dp))

            // 🔹 Botón para ver el historial de rentas
            Button(onClick = { onVerHistorialClick(vehiculo.id) }) {
                Text("Ver Historial")
            }
        }
    }
}

