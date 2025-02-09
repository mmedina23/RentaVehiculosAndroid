package com.pmd.rentavehiculos.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pmd.rentavehiculos.model.RentaRequest
import com.pmd.rentavehiculos.viewmodel.LoginViewModel
import com.pmd.rentavehiculos.viewmodel.VehiculosViewModel

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

    LaunchedEffect(apiKey, vehiculoId) {
        if (apiKey != null) {
            vehiculosViewModel.obtenerVehiculosRentadosAdmin(apiKey, vehiculoId)
        }
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text("Historial de Rentas", fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF0D47A1))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .background(Color(0xFFF5F5F5)) // Fondo más claro y moderno
        ) {
            Text(
                "Historial del vehículo ID: $vehiculoId",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D47A1)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (rentas.isEmpty()) {
                Text(
                    "No hay historial de rentas para este vehículo.",
                    modifier = Modifier.padding(16.dp),
                    color = Color.Gray,
                    fontSize = 16.sp
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "🚗 ${renta.vehiculo.marca} - ${renta.vehiculo.color}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D47A1)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "👤 Cliente: ${renta.persona.nombre} ${renta.persona.apellidos}", fontSize = 16.sp, color = Color.DarkGray)
            Text(text = "📆 Días rentados: ${renta.dias_renta}", fontSize = 16.sp, color = Color.DarkGray)
            Text(text = "💰 Valor total: $${renta.valor_total_renta}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
            Text(text = "📅 Fecha de renta: ${renta.fecha_renta}", fontSize = 16.sp, color = Color.DarkGray)
            Text(text = "📅 Fecha estimada de entrega: ${renta.fecha_estimada_entrega}", fontSize = 16.sp, color = Color.DarkGray)

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (!renta.fecha_entregado.isNullOrEmpty()) {
                    "✅ Entregado el: ${renta.fecha_entregado}"
                } else {
                    "⏳ No entregado aún"
                },
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = if (!renta.fecha_entregado.isNullOrEmpty()) Color(0xFF2E7D32) else Color(0xFFD32F2F)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewVehiculosRentadosAdminScreen() {
    VehiculosRentadosAdminScreen(
        navController = NavController(LocalContext.current),
        vehiculoId = 123
    )
}
