package com.pmd.rentavehiculos.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pmd.rentavehiculos.modelo.RentaRequest
import com.pmd.rentavehiculos.viewModels.LoginViewModel
import com.pmd.rentavehiculos.viewModels.VehiculosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehiculosRentadosAdminScreen(
    navController: NavController,
    vehiculoId: Int,
    vehiculosViewModel: VehiculosViewModel = viewModel(),
    loginViewModel: LoginViewModel = viewModel()
) {
    val apiKey = loginViewModel.apiKey.value
    val rentas = vehiculosViewModel.rentas

    // Cargar historial de rentas del vehículo al abrir la pantalla
    LaunchedEffect(apiKey, vehiculoId) {
        if (apiKey != null) {
            vehiculosViewModel.obtenerVehiculosRentadosAdmin(apiKey, vehiculoId)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Historial de Vehículos Rentados") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF800080), // Morado fuerte
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Viendo el historial del vehículo ID: $vehiculoId",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF9932CC), // Morado claro
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (rentas.isEmpty()) {
                Text(
                    text = "No hay historial de rentas para este vehículo.",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF4B0082) // Morado intermedio
                )
            } else {
                LazyColumn {
                    items(rentas) { renta ->
                        RentaCardAdmin(renta)
                    }
                }
            }
        }
    }
}

@Composable
fun RentaCardAdmin(renta: RentaRequest) {
    println("Imagen URL: ${renta.vehiculo.imagen}") // Depuración para verificar la URL de la imagen

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔹 Imagen del vehículo rentado
            AsyncImage(
                model = renta.vehiculo.imagen,
                contentDescription = "Imagen del Vehículo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 🔹 Información principal del vehículo rentado
            Text(
                text = "${renta.vehiculo.marca} - ${renta.vehiculo.color}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF800080) // Morado fuerte
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 🔹 Información detallada de la renta
            Text(
                text = "Cliente: ${renta.persona.nombre} ${renta.persona.apellidos}",
                color = Color.Black
            )
            Text(text = "Días rentados: ${renta.dias_renta}", color = Color.Black)
            Text(
                text = "Valor total: $${renta.valor_total_renta}",
                color = Color(0xFF9932CC) // Morado claro
            )
            Text(text = "Fecha de renta: ${renta.fecha_renta}", color = Color.Black)
            Text(
                text = "Fecha estimada de entrega: ${renta.fecha_estimada_entrega}",
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 🔹 Estado de la entrega
            Text(
                text = if (!renta.fecha_entregado.isNullOrEmpty()) {
                    "Entregado el: ${renta.fecha_entregado}"
                } else {
                    "No entregado aún"
                },
                fontWeight = FontWeight.Bold,
                color = if (!renta.fecha_entregado.isNullOrEmpty()) Color.Green else Color.Red
            )
        }
    }
}
