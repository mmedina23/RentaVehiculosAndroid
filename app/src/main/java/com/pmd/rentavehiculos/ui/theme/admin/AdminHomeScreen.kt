package com.pmd.rentavehiculos.ui.theme.admin

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pmd.rentavehiculos.data.model.Renta
import com.pmd.rentavehiculos.data.model.Vehiculo
import com.pmd.rentavehiculos.ui.theme.viewmodel.AdminViewModel

@Composable
fun AdminHomeScreen(
    navController: NavController,
    context: Context,
    id: String?
) {
    val viewModel: AdminViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return AdminViewModel(context) as T
            }
        }
    )

    LaunchedEffect(Unit) {
        viewModel.loadVehiculosDisponibles()
        viewModel.loadVehiculosRentados()
    }

    val vehiculosDisponibles by viewModel.vehiculosDisponibles.observeAsState(emptyList())
    val vehiculosRentados by viewModel.vehiculosRentados.observeAsState(emptyList())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Vehículos Disponibles", style = MaterialTheme.typography.titleMedium)
        LazyColumn {
            items(vehiculosDisponibles) { vehiculo ->
                VehiculoDisponibleCard(vehiculo)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Vehículos Rentados", style = MaterialTheme.typography.titleMedium)
        LazyColumn {
            items(vehiculosRentados) { renta ->
                VehiculoRentadoCard(renta)
            }
        }
    }
}

@Composable
fun VehiculoDisponibleCard(vehiculo: Vehiculo) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text("ID: ${vehiculo.id}", style = MaterialTheme.typography.bodyLarge)
        Text("Marca: ${vehiculo.marca}", style = MaterialTheme.typography.bodyMedium)
        Text("Carroceria: ${vehiculo.carroceria}", style = MaterialTheme.typography.bodyMedium)
        Text("Plazas: ${vehiculo.plazas}", style = MaterialTheme.typography.bodyMedium)
        Text("Color: ${vehiculo.color}", style = MaterialTheme.typography.bodyMedium)
        Text("Cambios: ${vehiculo.cambios}", style = MaterialTheme.typography.bodyMedium)
        Text("Tipo Combustible: ${vehiculo.tipo_combustible}", style = MaterialTheme.typography.bodyMedium)
        Text("Valor por Día: $${vehiculo.valorDia}", style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun VehiculoRentadoCard(renta: Renta) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text("ID Renta: ${renta.id}", style = MaterialTheme.typography.bodyLarge)
        Text("ID Vehículo: ${renta.vehiculoId}", style = MaterialTheme.typography.bodyMedium)
        Text("ID Persona: ${renta.personaId}", style = MaterialTheme.typography.bodyMedium)
        Text("Días Rentado: ${renta.dias_renta}", style = MaterialTheme.typography.bodyMedium)
        Text("Fecha de Renta: ${renta.fecha_renta}", style = MaterialTheme.typography.bodyMedium)
        Text("Fecha Estimada de Entrega: ${renta.fecha_estimada_entrega}", style = MaterialTheme.typography.bodyMedium)
        Text("Fecha de Entrega: ${renta.fecha_entregado ?: "No entregado"}", style = MaterialTheme.typography.bodyMedium)
        Text("Valor Total: $${renta.valor_total_venta}", style = MaterialTheme.typography.bodyMedium)
    }
}
