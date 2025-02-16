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
import com.pmd.rentavehiculos.data.model.Renta
import com.pmd.rentavehiculos.data.model.Vehiculo
import com.pmd.rentavehiculos.ui.theme.viewmodel.AdminViewModel
@Composable
fun ListaVehiculosRentados(
    navController: NavController,
    viewModel: AdminViewModel,
) {
    LaunchedEffect(Unit) {
        viewModel.loadVehiculosRentadosAdmin()
    }

    val vehiculosRentados by viewModel.vehiculosRentadosAdminLiveData.observeAsState(emptyList())
    val errorMessage by viewModel.errorLiveData.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("🚗 Vehículos Rentados", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        errorMessage?.let { error ->
            Text("❌ Error: $error", color = MaterialTheme.colorScheme.error)
        }

        if (vehiculosRentados.isNotEmpty()) {
            LazyColumn {
                items(vehiculosRentados) { vehiculoConRenta ->
                    VehiculoRentadoCard(vehiculo = vehiculoConRenta.vehiculo, renta = vehiculoConRenta.renta)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }


        } else {
            Text(
                "No hay vehículos rentados actualmente.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun VehiculoRentadoCard(vehiculo: Vehiculo, renta: Renta?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Datos del vehículo
            AsyncImage(
                model = vehiculo.imagen,
                contentDescription = "Imagen del vehículo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("🚗 Vehículo: ${vehiculo.marca}")
            Text("🎨 Color: ${vehiculo.color}")
            Text("🚗 Carrocería: ${vehiculo.carroceria}")
            Text("🚙 Plazas: ${vehiculo.plazas}")
            Text("⛽ Combustible: ${vehiculo.tipo_combustible}")

            if (renta != null) {
                // Datos de la persona que alquiló el vehículo
                val persona = renta.persona
                Text("👤 Persona que lo rentó:")
                Text("  Nombre: ${persona.nombre} ${persona.apellidos}")
                Text("  Dirección: ${persona.direccion}")
                Text("  Teléfono: ${persona.telefono}")
                Text("  Identificación: ${persona.identificacion}")

                // Datos de la renta
                Text("🗓️ Días Rentados: ${renta.dias}")
                Text("📅 Fecha de Alquiler: ${renta.fechaRenta}")
                Text("📆 Fecha Estimada de Entrega: ${renta.fechaPrevistaEntrega}")
                Text("📅 Fecha de Entrega: ${renta.fechaEntrega ?: "No entregado"}")
                Text("💰 Valor Total de la Renta: $${renta.valorTotal}")
            } else {
                Text("⚠️ Este vehículo está marcado como NO DISPONIBLE pero no tiene historial de renta.")
            }
        }
    }
}







