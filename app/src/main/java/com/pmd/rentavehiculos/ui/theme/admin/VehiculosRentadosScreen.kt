package com.pmd.rentavehiculos.ui.theme.admin

import android.content.Context
import androidx.compose.foundation.clickable
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
                    VehiculoRentadoCard(
                        vehiculo = vehiculoConRenta.vehiculo,
                        onClick = {
                            navController.navigate("detalle_vehiculo_rentado/${vehiculoConRenta.vehiculo.id}")
                        }
                    )
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
fun VehiculoRentadoCard(vehiculo: Vehiculo, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }, // Al hacer clic, se navega a los detalles
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
            Text("🚗 Vehículo: ${vehiculo.marca}")
            Text("🎨 Color: ${vehiculo.color}")
            Text("🚗 Carrocería: ${vehiculo.carroceria}")
            Text("🚙 Plazas: ${vehiculo.plazas}")
            Text("⛽ Combustible: ${vehiculo.tipo_combustible}")

            Spacer(modifier = Modifier.height(8.dp))

            // Botón para ver detalles completos
            Button(onClick = onClick) {
                Text("Ver Detalles")
            }
        }
    }
}
@Composable
fun DetalleVehiculoRentadoScreen(
    navController: NavController,
    viewModel: AdminViewModel,
    vehiculoId: Int
) {
    // Cargar la información del historial de rentas del vehículo
    LaunchedEffect(Unit) {
        viewModel.obtenerHistorialRentas(viewModel.obtenerToken() ?: "", vehiculoId)
    }

    val historialRentas by viewModel.rentasLiveData.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = { navController.popBackStack() }) {
            Text("⬅ Volver")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (historialRentas.isNotEmpty()) {
            val renta = historialRentas.last() // Tomar la última renta como la más reciente
            VehiculoRentadoDetalleCard(vehiculo = renta.vehiculo, renta = renta) {
                navController.navigate("historial_rentas/$vehiculoId")
            }
        } else {
            Text("No hay historial de rentas para este vehículo.")
        }
    }
}
@Composable
fun VehiculoRentadoDetalleCard(
    vehiculo: Vehiculo,
    renta: Renta,
    onVerHistorialClick: (Int) -> Unit
) {
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
            Text("🚗 Vehículo: ${vehiculo.marca}")
            Text("🎨 Color: ${vehiculo.color}")
            Text("🚗 Carrocería: ${vehiculo.carroceria}")
            Text("🚙 Plazas: ${vehiculo.plazas}")
            Text("⛽ Combustible: ${vehiculo.tipo_combustible}")

            Text("👤 Persona que lo rentó:")
            Text("  Nombre: ${renta.persona.nombre} ${renta.persona.apellidos}")
            Text("  Dirección: ${renta.persona.direccion}")
            Text("  Teléfono: ${renta.persona.telefono}")
            Text("  Identificación: ${renta.persona.identificacion}")

            Text("🗓️ Días Rentados: ${renta.dias}")
            Text("📅 Fecha de Alquiler: ${renta.fechaRenta}")
            Text("📆 Fecha Estimada de Entrega: ${renta.fechaPrevistaEntrega}")
            Text("📅 Fecha de Entrega: ${renta.fechaEntrega ?: "No entregado"}")
            Text("💰 Valor Total de la Renta: $${renta.valorTotal}")

            Spacer(modifier = Modifier.height(8.dp))

            // 🔹 Botón para ver el historial de rentas
            Button(onClick = { onVerHistorialClick(vehiculo.id) }) {
                Text("Ver Historial de Rentas")
            }
        }
    }
}










