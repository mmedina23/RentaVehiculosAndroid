package com.pmd.rentavehiculos.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pmd.rentavehiculos.model.RentaSolicitud
import com.pmd.rentavehiculos.vista.VistaLogin
import com.pmd.rentavehiculos.vista.VistaVehiculos
import com.pmd.rentavehiculos.model.Vehiculo
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehiculosRentadosScreen(
    navController: NavController,
    vehiculosViewModel: VistaVehiculos = viewModel(),
    loginViewModel: VistaLogin = viewModel()
) {
    val apiKey = loginViewModel.apiKey.value
    val personaId = loginViewModel.usuario.value?.id
    val rentas = vehiculosViewModel.rentas
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(apiKey, personaId) {
        if (apiKey != null && personaId != null) {
            vehiculosViewModel.obtenerVehiculosRentados(apiKey, personaId)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Mis Vehículos Alquilados",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0D77A1))
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


            if (rentas.isEmpty()) {
                Text("No tienes vehículos alquilados.", modifier = Modifier.padding(16.dp))
            } else {
                LazyColumn {
                    items(rentas) { renta ->
                        RentaCard(renta) { vehiculoId ->
                            if (apiKey != null) {
                                vehiculosViewModel.entregarVehiculo(apiKey, vehiculoId) { success, message ->
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(message)
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
fun RentaCard(renta: RentaSolicitud, onEntregarVehiculo: (Int) -> Unit) {
    val yaEntregado = !renta.fecha_entregado.isNullOrEmpty()


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (yaEntregado) Color(0xFFE0E0E0) else Color.White
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = renta.vehiculo.marca,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = if (yaEntregado) Color.Gray else Color.Black
            )
            AsyncImage(
                model = renta.vehiculo.imagen,
                contentDescription = "Imagen del Vehículo",
                modifier = Modifier.fillMaxSize()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Días alquilados: ${renta.dias_renta}")
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Total: ${renta.valor_total_renta} €")
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Fecha alquiler: ${renta.fecha_renta}")
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Fecha entrega estimada: ${renta.fecha_estimada_entrega}")

            if (yaEntregado) {
                Text(
                    text = "✅ Vehículo entregado el: ${renta.fecha_entregado}",
                    fontWeight = FontWeight.Bold,
                    color = Color.Green
                )
            } else {
                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { onEntregarVehiculo(renta.vehiculo.id) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.width(180.dp)
                ) {
                    Text("Entregar Vehículo", color = Color.White)
                }
            }
        }
    }
}






