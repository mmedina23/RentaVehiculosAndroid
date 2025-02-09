package com.pmd.rentavehiculos.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pmd.rentavehiculos.model.RentaRequest
import com.pmd.rentavehiculos.viewmodel.LoginViewModel
import com.pmd.rentavehiculos.viewmodel.VehiculosViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehiculosRentadosScreen(
    navController: NavController,
    vehiculosViewModel: VehiculosViewModel = viewModel(),
    loginViewModel: LoginViewModel = viewModel()
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
                        fontSize = 26.sp,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF001F3F), Color(0xFF003366))
                    )
                )
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (rentas.isEmpty()) {
                Text(
                    text = "No tienes vehículos alquilados.",
                    fontSize = 18.sp,
                    color = Color.LightGray,
                    modifier = Modifier.padding(16.dp)
                )
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
fun RentaCard(renta: RentaRequest, onEntregarVehiculo: (Int) -> Unit) {
    val yaEntregado = !renta.fecha_entregado.isNullOrEmpty()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (yaEntregado) Color(0xFFCCCCCC) else Color(0xFFE3F2FD)
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = renta.vehiculo.marca,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = if (yaEntregado) Color.Gray else Color(0xFF003366)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Días alquilados: ${renta.dias_renta}", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Total: ${renta.valor_total_renta} €", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Fecha alquiler: ${renta.fecha_renta}", fontSize = 14.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Fecha entrega estimada: ${renta.fecha_estimada_entrega}", fontSize = 14.sp)

            if (yaEntregado) {
                Text(
                    text = "✅ Vehículo entregado el: ${renta.fecha_entregado}",
                    fontWeight = FontWeight.Bold,
                    color = Color.Green,
                    fontSize = 16.sp
                )
            } else {
                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = { onEntregarVehiculo(renta.vehiculo.id) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                    modifier = Modifier
                        .width(200.dp)
                        .padding(vertical = 12.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Entregar Vehículo", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
