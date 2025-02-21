package com.pmd.rentavehiculos.ui.theme.cliente

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
    onLogoutSuccess: () -> Unit // <-- Agregamos esta función
) {
    val vehiculosDisponibles by viewModel.vehiculosDisponibles.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.cargarVehiculosDisponibles()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Renta de Vehículos") },
                actions = {
                    TextButton(onClick = { navController.navigate("rentas_actuales") }) {
                        Text("Ver Rentas Actuales")
                    }
                    Button(
                        onClick = {
                            viewModel.logout(
                                onLogoutSuccess = { onLogoutSuccess() }, // Llamamos al callback de logout
                                onLogoutError = { errorMessage -> println(errorMessage) }
                            )
                        }
                    ) {
                        Text("Cerrar Sesión")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("Vehículos Disponibles", style = MaterialTheme.typography.headlineMedium)

            LazyColumn {
                items(vehiculosDisponibles) { vehiculo ->
                    VehiculoDisponibleCard(vehiculo) {
                        navController.navigate("detalle_vehiculo/${vehiculo.id}")
                    }
                }
            }
        }
    }
}



@Composable
fun VehiculoDisponibleCard(vehiculo: Vehiculo, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp).clickable { onClick() },
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
            Text("Valor por Día: $${"%.2f".format(vehiculo.valor_dia)}")
        }
    }
}

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
