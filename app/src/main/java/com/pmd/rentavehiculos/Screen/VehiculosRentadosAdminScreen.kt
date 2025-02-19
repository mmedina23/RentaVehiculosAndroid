package com.pmd.rentavehiculos.Screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pmd.rentavehiculos.data.model.RentaRquest
import com.pmd.rentavehiculos.viewmodel.LoginViewModel
import com.pmd.rentavehiculos.viewmodel.VehiculosViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehiculosRentadosAdminScreen(navController: NavController, vehiculoId: Int, vehiculosViewModel: VehiculosViewModel = viewModel(), loginViewModel: LoginViewModel = viewModel()) {
    val apiKey = loginViewModel.apiKey.value
    val rentas = vehiculosViewModel.rentas

    LaunchedEffect(apiKey, vehiculoId) {
        if (apiKey != null) {
            vehiculosViewModel.obtenerVehiculosRentadosAdmin(apiKey, vehiculoId)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Historial de Rentas") }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0077B7)))
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("Viendo el historial del vehículo ID: $vehiculoId", fontSize = 18.sp, fontWeight = FontWeight.Bold)

            if (rentas.isEmpty()) {
                Text("No hay historial de rentas para este vehículo.", modifier = Modifier.padding(16.dp))
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
fun RentaCardAdmin(renta: RentaRquest) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "🚗 ${renta.vehiculo.marca} - ${renta.vehiculo.color}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "👤 Cliente: ${renta.persona.nombre} ${renta.persona.apellidos}")
            Text(text = "📆 Días rentados: ${renta.dias_renta}")
            Text(text = "💰 Valor total: $${renta.valor_total_renta}")
            Text(text = "📅 Fecha de renta: ${renta.fecha_renta}")
            Text(text = "📅 Fecha estimada de entrega: ${renta.fecha_estimada_entrega}")

            Text(
                text = if (!renta.fecha_entregado.isNullOrEmpty()) {
                    "✅ Entregado el: ${renta.fecha_entregado}"
                } else {
                    "⏳ No entregado aún"
                },
                fontWeight = FontWeight.Bold,
                color = if (!renta.fecha_entregado.isNullOrEmpty()) Color.Green else Color.Red
            )
        }
    }
}