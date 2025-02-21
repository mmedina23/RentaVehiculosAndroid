package com.pmd.rentavehiculos.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pmd.rentavehiculos.model.RentaSolicitud
import com.pmd.rentavehiculos.vista.VistaLogin
import com.pmd.rentavehiculos.vista.VistaVehiculos

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehiculosRentadosAdminScreen(
    navController: NavController,
    vehiculoId: Int,
    vehiculosViewModel: VistaVehiculos = viewModel(),
    loginViewModel: VistaLogin = viewModel()
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
            CenterAlignedTopAppBar(
                title = { Text("🚗 DriveGo Admin - Historial de Rentas", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF0077B7),
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
                .background(Brush.verticalGradient(listOf(Color(0xFFEEF2F3), Color.White)))
        ) {
            Text(
                "📋 Historial del Vehículo ID: $vehiculoId",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0066A2),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (rentas.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "🚫 No hay historial de rentas para este vehículo.",
                        fontSize = 18.sp,
                        color = Color.Gray
                    )
                }
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
fun RentaCardAdmin(renta: RentaSolicitud) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 12.dp)
            .shadow(6.dp, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "🚗 ${renta.vehiculo.marca} - ${renta.vehiculo.color}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "👤 Cliente: ${renta.persona.nombre} ${renta.persona.apellidos}", fontSize = 16.sp, color = Color.Gray)
            Text(text = "📆 Días rentados: ${renta.dias_renta}", fontSize = 16.sp, color = Color.Gray)
            Text(
                text = "💰 Valor total: $${renta.valor_total_renta}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00A86B)
            )
            Text(text = "📅 Fecha de renta: ${renta.fecha_renta}", fontSize = 16.sp, color = Color.Gray)
            Text(text = "📅 Fecha estimada de entrega: ${renta.fecha_estimada_entrega}", fontSize = 16.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (!renta.fecha_entregado.isNullOrEmpty()) {
                    "✅ Entregado el: ${renta.fecha_entregado}"
                } else {
                    "⏳ No entregado aún"
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (!renta.fecha_entregado.isNullOrEmpty()) Color.Green else Color.Red
            )
        }
    }
}
