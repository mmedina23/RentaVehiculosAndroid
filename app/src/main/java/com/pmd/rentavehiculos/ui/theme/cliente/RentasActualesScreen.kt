package com.pmd.rentavehiculos.ui.theme.cliente

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pmd.rentavehiculos.data.model.Renta
import com.pmd.rentavehiculos.ui.theme.viewmodel.ClienteViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RentasActualesScreen(viewModel: ClienteViewModel, navController: NavController) {
    val vehiculosRentados by viewModel.vehiculosRentados.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.cargarVehiculosRentados()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mis Rentas Actuales") }) }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
            if (vehiculosRentados.isEmpty()) {
                Text("No tienes vehículos rentados actualmente.", style = MaterialTheme.typography.bodyLarge, color = Color.White)
            } else {
                LazyColumn {
                    items(vehiculosRentados) { renta ->
                        VehiculoRentadoClienteCard(renta) {
                            scope.launch {
                                viewModel.entregarVehiculo(renta) { success, message ->
                                    if (success) {
                                        viewModel.cargarVehiculosDisponibles()
                                        viewModel.cargarVehiculosRentados()
                                        navController.navigate("cliente_home") {
                                            popUpTo("rentas_actuales") { inclusive = true }
                                        }
                                    }
                                }
                            }
                        }
                        Divider(thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp), color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun VehiculoRentadoClienteCard(renta: Renta, onEntregarClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(10.dp, RoundedCornerShape(16.dp))
            .border(2.dp, Color.White, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = renta.vehiculo.imagen,
                contentDescription = "Imagen del vehículo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Marca: ${renta.vehiculo.marca}", color = Color.White, style = MaterialTheme.typography.bodyLarge)
            Text("Modelo: ${renta.vehiculo.carroceria}", color = Color.White, style = MaterialTheme.typography.bodyLarge)
            Text("Fecha Renta: ${renta.fechaRenta}", color = Color.White)
            Text("Fecha Entrega: ${renta.fechaPrevistaEntrega}", color = Color.White)
            Text("🗓️ Días Rentados: ${renta.dias}", color = Color.White)
            Text("Valor Total: $${renta.valorTotal}", color = Color.White)
            Button(onClick = onEntregarClick, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)), ) {
                Text("Entregar",  color = Color.White)
            }
        }
    }
}