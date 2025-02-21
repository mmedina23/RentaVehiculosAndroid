package com.pmd.rentavehiculos.ui.theme.cliente

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pmd.rentavehiculos.data.model.Renta
import com.pmd.rentavehiculos.ui.theme.viewmodel.ClienteViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RentasActualesScreen(viewModel: ClienteViewModel, navController: NavController) {
    val vehiculosRentados by viewModel.vehiculosRentados.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.cargarVehiculosRentados()  // 🔹 Recargar rentas al entrar a la pantalla
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mis Rentas Actuales") }) }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
            if (vehiculosRentados.isEmpty()) {
                Text("No tienes vehículos rentados actualmente.", style = MaterialTheme.typography.bodyLarge)
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
                    }
                }
            }
        }
    }
}

@Composable
fun VehiculoRentadoClienteCard(renta: Renta, onEntregarClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("ID: ${renta.id}")
            Text("Marca: ${renta.vehiculo.marca}")
            Text("Fecha Renta: ${renta.fechaRenta}")
            Text("Fecha Entrega: ${renta.fechaPrevistaEntrega}")
            Text("Valor Total: $${renta.valorTotal}")
            Button(onClick = onEntregarClick) {
                Text("Entregar")
            }
        }
    }
}