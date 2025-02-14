package com.pmd.rentavehiculos.ui.theme.admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pmd.rentavehiculos.data.model.Renta
import com.pmd.rentavehiculos.data.model.Vehiculo
import com.pmd.rentavehiculos.ui.theme.viewmodel.AdminViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun AdminHomeScreen(
    navController: NavController,
    viewModel: AdminViewModel = viewModel(),
    llaveApi: String
) {
    viewModel.llaveApi = llaveApi
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

// Card para un vehículo disponible
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
        Text("Carroceria: ${vehiculo.disponible}", style = MaterialTheme.typography.bodyMedium)
    }
}

// Card para un vehículo rentado
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
        Text(
            "Fecha de Entrega: ${renta.fecha_entregado ?: "No entregado"}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text("Valor Total: $${renta.valor_total_venta}", style = MaterialTheme.typography.bodyMedium)
    }
}
