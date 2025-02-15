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
import com.pmd.rentavehiculos.data.model.Renta
import com.pmd.rentavehiculos.data.model.RentarVehiculoRequest
import com.pmd.rentavehiculos.data.model.Vehiculo
import com.pmd.rentavehiculos.ui.theme.viewmodel.AdminViewModel
@Composable
fun ListaVehiculosRentados(navController: NavController, context: Context) {
    val viewModel: AdminViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return AdminViewModel(context) as T
            }
        }
    )

    // Obtenemos el estado de las rentas (que contienen los vehículos)
    val rentas by viewModel.rentas.observeAsState(emptyList())

    // Cargar las rentas
    LaunchedEffect(Unit) {
        viewModel.loadVehiculosRentados() // Cargar las rentas (y, por ende, los vehículos rentados)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("🚗 Vehículos Rentados", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar rentas (y los vehículos asociados a esas rentas)
        if (rentas.isNotEmpty()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(rentas) { renta ->
                    val vehiculo = renta.vehiculo // Obtener el vehículo asociado a la renta
                    VehiculoRentadoCard(vehiculo = vehiculo, renta = renta) // Mostrar detalles del vehículo rentado
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        } else {
            Text("No se encontraron vehículos rentados en este momento.", style = MaterialTheme.typography.bodyMedium)
        }
    }
}



@Composable
fun VehiculoRentadoCard(vehiculo: Vehiculo, renta: Renta) {
    val persona = renta.persona

    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Datos del vehículo
            Text("🚗 Vehículo: ${vehiculo.marca}")
            Text("🔑 ID del Vehículo: ${vehiculo.id}")
            Text("🎨 Color: ${vehiculo.color}")
            Text("🚗 Carrocería: ${vehiculo.carroceria}")
            Text("🚙 Plazas: ${vehiculo.plazas}")
            Text("⛽ Combustible: ${vehiculo.tipo_combustible}")

            // Datos de la persona que alquiló el vehículo
            Text("👤 Persona que lo rentó:")
            Text("  ID Persona: ${persona.id}")
            Text("  Nombre: ${persona.nombre} ${persona.apellidos}")
            Text("  Dirección: ${persona.direccion}")
            Text("  Teléfono: ${persona.telefono}")
            Text("  Identificación: ${persona.identificacion}")

            // Datos de la renta
            Text("🗓️ Días Rentados: ${renta.dias}")
            Text("📅 Fecha de Alquiler: ${renta.fechaRenta}")
            Text("📆 Fecha Estimada de Entrega: ${renta.fechaPrevistaEntrega}")
            Text("📅 Fecha de Entrega: ${renta.fechaEntrega ?: "No entregado"}")

            // Valor de la renta
            Text("💰 Valor Total de la Renta: $${renta.valorTotal}")
        }
    }
}




