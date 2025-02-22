package com.pmd.rentavehiculos.ui.theme.admin

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🚗 Vehículos Rentados", style = MaterialTheme.typography.headlineMedium, color = Color.White)
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
                    Divider(thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp), color = Color.White)
                }
            }
        } else {
            Text(
                "No hay vehículos rentados actualmente.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
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
            .shadow(10.dp, RoundedCornerShape(16.dp))
            .border(2.dp, Color.White, RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = vehiculo.imagen,
                contentDescription = "Imagen del vehículo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("🚗 Vehículo: ${vehiculo.marca}", style = MaterialTheme.typography.bodyLarge, color = Color.White)
            Text("🎨 Color: ${vehiculo.color}", color = Color.White)
            Text("🚗 Carrocería: ${vehiculo.carroceria}", color = Color.White)
            Text("🚙 Plazas: ${vehiculo.plazas}", color = Color.White)
            Text("⛽ Combustible: ${vehiculo.tipo_combustible}", color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
            ) {
                Text("Ver Detalles", color = Color.White)
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


        Spacer(modifier = Modifier.height(20.dp))

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
            .padding(8.dp)
            .shadow(10.dp, RoundedCornerShape(16.dp))
            .border(2.dp, Color.White, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = vehiculo.imagen,
                contentDescription = "Imagen del vehículo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("🚗 Vehículo: ${vehiculo.marca}", style = MaterialTheme.typography.bodyLarge, color = Color.White)
            Text("🎨 Color: ${vehiculo.color}", color = Color.White)
            Text("🚗 Carrocería: ${vehiculo.carroceria}", color = Color.White)
            Text("🚙 Plazas: ${vehiculo.plazas}", color = Color.White)
            Text("⛽ Combustible: ${vehiculo.tipo_combustible}", color = Color.White)

            Text("👤 Persona que lo rentó:", color = Color.White)
            Text("  Nombre: ${renta.persona.nombre} ${renta.persona.apellidos}", color = Color.White)
            Text("  Dirección: ${renta.persona.direccion}", color = Color.White)
            Text("  Teléfono: ${renta.persona.telefono}", color = Color.White)
            Text("  Identificación: ${renta.persona.identificacion}", color = Color.White)

            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🗓️ Días Rentados: ${renta.dias}", color = Color.White)
                    Text("📅 Fecha de Alquiler:", color = Color.White)
                    Text(renta.fechaRenta, color = Color.White)
                    Text("📆 Fecha Estimada de Entrega:", color = Color.White)
                    Text(renta.fechaPrevistaEntrega, color = Color.White)
                    Text("📅 Fecha de Entrega: ${renta.fechaEntrega ?: "No entregado"}", color = Color.White)
                    Text("💰 Valor Total de la Renta: $${renta.valorTotal}", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { onVerHistorialClick(vehiculo.id) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
            ) {
                Text("Ver Historial de Rentas", color = Color.White)
            }
        }
    }
}










