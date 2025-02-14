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
import com.pmd.rentavehiculos.data.model.Vehiculo
import com.pmd.rentavehiculos.ui.theme.viewmodel.AdminViewModel

@Composable
fun ListaVehiculosRentados(navController: NavController, context : Context) {
    val viewModel: AdminViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return AdminViewModel(context) as T
            }
        }
    )

    // Obtenemos el estado de los vehículos rentados
    val vehiculosRentados by viewModel.vehiculosRentados.observeAsState(emptyList())

    // Cargar los vehículos rentados
    LaunchedEffect(Unit) {
        viewModel.loadVehiculosRentados() // Cargar los vehículos rentados
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("🚗 Vehículos Rentados", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar Vehículos Rentados
        if (vehiculosRentados.isNotEmpty()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(vehiculosRentados) { vehiculo ->
                    VehiculoRentadoCard(vehiculo) // Mostrar cada vehículo rentado
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        } else {
            Text("No se encontraron vehículos rentados en este momento.", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun VehiculoRentadoCard(vehiculo: Vehiculo) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Marca: ${vehiculo.marca}", style = MaterialTheme.typography.bodyLarge)
            Text("Modelo: ${vehiculo.carroceria}")
            Text("Color: ${vehiculo.color}")
            Text("Plazas: ${vehiculo.plazas}")
            Text("Tipo Combustible: ${vehiculo.tipo_combustible}")
            Text("Valor por Día: $${vehiculo.valorDia}")
        }
    }
}
