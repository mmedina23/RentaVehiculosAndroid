package com.pmd.rentavehiculos.ui.theme.cliente

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pmd.rentavehiculos.data.model.Renta
import com.pmd.rentavehiculos.data.model.Vehiculo
import com.pmd.rentavehiculos.ui.theme.viewmodel.ClienteViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ClienteHomeScreen(
    viewModel: ClienteViewModel,
    navController: NavController,
) {
    val vehiculosDisponibles by viewModel.vehiculosDisponibles.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Renta de Vehículos") },
                actions = {
                    TextButton(onClick = { navController.navigate("rentas_actuales") }) {
                        Text("Ver Rentas Actuales")
                    }
                    TextButton(onClick = { navController.navigate("historial_rentas") }) {
                        Text("Ver Historial de Rentas")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("Vehículos Disponibles", style = MaterialTheme.typography.headlineMedium)

            LazyColumn {
                items(vehiculosDisponibles) { vehiculo ->
                    VehiculoDisponibleCard(vehiculo) { diasRenta ->
                        scope.launch {
                            viewModel.rentarVehiculo(vehiculo, diasRenta) { success, message ->
                                scope.launch {
                                    val snackbarMessage = if (success) "✅ Renta exitosa: $message"
                                    else "❌ Error al rentar: $message"

                                    snackbarHostState.showSnackbar(
                                        snackbarMessage,
                                        duration = SnackbarDuration.Short
                                    )
                                }

                                if (success) {
                                    viewModel.cargarVehiculosDisponibles()
                                    navController.navigate("rentas_actuales") {
                                        popUpTo(navController.graph.startDestinationId) { inclusive = false }
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
fun VehiculoDisponibleCard(vehiculo: Vehiculo, onRentarClick: (Int) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    var diasRenta by remember { mutableStateOf("1") }
    val valorPorDia = vehiculo.valor_dia ?: 0.0
    var totalCosto by remember { mutableStateOf(valorPorDia) }

    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            AsyncImage(
                model = vehiculo.imagen,
                contentDescription = "Imagen del vehículo",
                modifier = Modifier.fillMaxWidth().height(200.dp)
            )
            Text("Marca: ${vehiculo.marca}")
            Text("Modelo: ${vehiculo.carroceria}")
            Text("Valor por Día: $${"%.2f".format(valorPorDia)}")

            Button(onClick = { showDialog = true }) {
                Text("Rentar")
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Seleccionar días de renta") },
            text = {
                Column {
                    Text("Ingrese la cantidad de días:")
                    OutlinedTextField(
                        value = diasRenta,
                        onValueChange = { input ->
                            if (input.all { it.isDigit() }) {
                                diasRenta = input
                                val dias = input.toIntOrNull()?.coerceAtLeast(1) ?: 1
                                totalCosto = dias * valorPorDia
                            }
                        },
                        label = { Text("Días") },
                        singleLine = true
                    )
                    Text("Costo Total: $${"%.2f".format(totalCosto)}")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val dias = diasRenta.toIntOrNull()?.coerceAtLeast(1) ?: 1
                        onRentarClick(dias)
                        showDialog = false
                    },
                    enabled = diasRenta.isNotEmpty()
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialRentasScreen(viewModel: ClienteViewModel) {
    val historialRentas by viewModel.historialRentas.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Historial de Rentas") })
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
            if (historialRentas.isEmpty()) {
                Text("No tienes vehículos en el historial.", style = MaterialTheme.typography.bodyLarge)
            } else {
                LazyColumn {
                    items(historialRentas) { renta ->
                        VehiculoRentadoClienteCard(renta) {}
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RentasActualesScreen(viewModel: ClienteViewModel, navController: NavController) {
    val vehiculosRentados by viewModel.vehiculosRentados.collectAsState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mis Rentas Actuales") })
        }
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

                                        // 🔹 Navegar a `cliente_home` para reflejar los cambios en la UI
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

