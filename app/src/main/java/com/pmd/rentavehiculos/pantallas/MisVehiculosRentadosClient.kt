package com.pmd.rentavehiculos.pantallas

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.pmd.rentavehiculos.modelo.RentaRequest
import com.pmd.rentavehiculos.viewModels.LoginViewModel
import com.pmd.rentavehiculos.viewModels.VehiculosViewModel
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
                        text = "Mis Vehículos Rentados",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF800080))
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
                Text(
                    text = "No tienes vehículos alquilados.",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF9932CC)
                )
            } else {
                LazyColumn {
                    items(rentas) { renta ->
                        RentaCard(renta) { vehiculoId ->
                            if (apiKey != null) {
                                vehiculosViewModel.entregarVehiculo(
                                    apiKey,
                                    vehiculoId
                                ) { success, message ->
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
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (yaEntregado) Color(0xFFE0E0E0) else Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Obtener la URL de la imagen del vehículo, si es null usar una imagen por defecto
            val imagenUrl = renta.vehiculo.imagen ?: "https://upload.wikimedia.org/wikipedia/commons/3/3a/Cat03.jpg"

            // Imprimir la URL de la imagen en el log
            println("Cargando imagen de ${renta.vehiculo.marca}: $imagenUrl")

            Image(
                painter = rememberAsyncImagePainter(imagenUrl),
                contentDescription = "Imagen del Vehículo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(MaterialTheme.shapes.medium)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = renta.vehiculo.marca,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = if (yaEntregado) Color.Gray else Color(0xFF800080)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Días alquilados: ${renta.dias_renta}", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Total: ${renta.valor_total_renta} €",
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF9932CC)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Fecha de alquiler: ${renta.fecha_renta}", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Fecha entrega estimada: ${renta.fecha_estimada_entrega}", fontWeight = FontWeight.SemiBold)

            if (yaEntregado) {
                Text(
                    text = "Vehículo entregado el: ${renta.fecha_entregado}",
                    fontWeight = FontWeight.Bold,
                    color = Color.Green
                )
            } else {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { onEntregarVehiculo(renta.vehiculo.id) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF800080)),
                    modifier = Modifier.width(180.dp)
                ) {
                    Text("Entregar Vehículo", color = Color.White)
                }
            }
        }
    }
}
