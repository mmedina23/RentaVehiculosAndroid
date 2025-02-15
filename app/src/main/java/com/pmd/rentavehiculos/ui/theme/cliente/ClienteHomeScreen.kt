package com.pmd.rentavehiculos.ui.theme.cliente

import android.annotation.SuppressLint
import android.content.Context
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
    context: Context
) {
    val vehiculosDisponibles by viewModel.vehiculosDisponibles.collectAsState()
    val vehiculosRentados by viewModel.vehiculosRentados.collectAsState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Renta de Vehículos") })
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
            Text("Vehículos Disponibles", style = MaterialTheme.typography.headlineMedium)
            LazyColumn {
                items(vehiculosDisponibles) { vehiculo ->
                    VehiculoDisponibleCard(vehiculo) {
                        scope.launch {
                            viewModel.rentarVehiculo(vehiculo) { success, message ->
                                if (success) {
                                    println("✅ Renta exitosa: $message")
                                } else {
                                    println("❌ Error al rentar: $message")
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Vehículos Rentados", style = MaterialTheme.typography.headlineMedium)
            LazyColumn {
                items(vehiculosRentados) { renta ->
                    VehiculoRentadoCard(renta) {
                        scope.launch {
                            viewModel.entregarVehiculo(renta) { success, message ->
                                if (success) {
                                    println("✅ Vehículo entregado: $message")
                                } else {
                                    println("❌ Error al entregar: $message")
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
fun VehiculoDisponibleCard(vehiculo: Vehiculo, onRentarClick: () -> Unit) {
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
            Text("Valor por Día: $${vehiculo.valor_dia}")
            Button(onClick = onRentarClick) {
                Text("Rentar")
            }
        }
    }
}

@Composable
fun VehiculoRentadoCard(renta: Renta, onEntregarClick: () -> Unit) {
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
