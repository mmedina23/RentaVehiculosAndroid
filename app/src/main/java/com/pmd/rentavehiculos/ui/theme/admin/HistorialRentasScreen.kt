package com.pmd.rentavehiculos.ui.theme.admin

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pmd.rentavehiculos.data.model.Renta
import com.pmd.rentavehiculos.ui.theme.viewmodel.AdminViewModel


@Composable
fun HistorialRentasScreen(
    viewModel: AdminViewModel,
    vehiculoId: Int,
    navController: NavController
) {
    val rentas by viewModel.rentasLiveData.observeAsState(emptyList())

    // Llamar a la API cuando se carga la pantalla
    LaunchedEffect(Unit) {
        val apiKey = viewModel.obtenerToken()
        if (apiKey != null) {
            viewModel.obtenerHistorialRentas(apiKey, vehiculoId)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("📜 Historial de Rentas", style = MaterialTheme.typography.headlineMedium, color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))

        if (rentas.isEmpty()) {
            Text("No hay historial de rentas para este vehículo.", color = Color.White)
        } else {
            LazyColumn {
                items(rentas) { renta ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .shadow(10.dp, RoundedCornerShape(16.dp))
                            .border(2.dp, Color.White, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("📅 Fecha de Alquiler:", color = Color.White)
                            Text(renta.fechaRenta, color = Color.White, modifier = Modifier.align(Alignment.CenterHorizontally))
                            Text("📆 Fecha Estimada de Entrega:", color = Color.White)
                            Text(renta.fechaPrevistaEntrega, color = Color.White, modifier = Modifier.align(Alignment.CenterHorizontally))
                            Text("📅 Fecha de Entrega:", color = Color.White)
                            Text(renta.fechaEntrega ?: "No entregado", color = Color.White, modifier = Modifier.align(Alignment.CenterHorizontally))
                            Text("💰 Valor Total: $${renta.valorTotal}", color = Color.White)
                            Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color.White)
                        }
                    }
                }
            }
        }
    }
}



