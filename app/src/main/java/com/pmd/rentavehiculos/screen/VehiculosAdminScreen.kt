package com.pmd.rentavehiculos.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pmd.rentavehiculos.model.Vehiculo
import com.pmd.rentavehiculos.vista.VistaLogin
import com.pmd.rentavehiculos.vista.VistaVehiculos

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehiculosAdminScreen(
    navController: NavController,
    vehiculosViewModel: VistaVehiculos = viewModel(),
    loginViewModel: VistaLogin = viewModel()
) {
    val apiKey = loginViewModel.apiKey.value
    val vehiculosDisponibles = vehiculosViewModel.vehiculosDisponibles

    LaunchedEffect(apiKey) {
        if (apiKey != null) {
            vehiculosViewModel.obtenerVehiculosDisponibles(apiKey)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("🚗 DriveGo Admin", fontSize = 22.sp, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF0066A2),
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
                text = "Gestión de Vehículos",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0066A2),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (vehiculosDisponibles.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay vehículos disponibles 😞",
                        fontSize = 18.sp,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn {
                    items(vehiculosDisponibles) { vehiculo ->
                        VehiculoAdminCard(vehiculo)
                    }
                }
            }
        }
    }
}

@Composable
fun VehiculoAdminCard(vehiculo: Vehiculo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 12.dp)
            .shadow(6.dp, shape = RoundedCornerShape(16.dp))
            .animateContentSize(
                animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
            ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = vehiculo.imagen,
                contentDescription = "Imagen del Vehículo",
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${vehiculo.marca} - ${vehiculo.color}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(text = "Carrocería: ${vehiculo.carroceria}", fontSize = 14.sp, color = Color.Gray)
                Text(text = "Plazas: ${vehiculo.plazas}", fontSize = 14.sp, color = Color.Gray)
                Text(text = "Cambio: ${vehiculo.cambios}", fontSize = 14.sp, color = Color.Gray)
                Text(text = "Combustible: ${vehiculo.tipo_combustible}", fontSize = 14.sp, color = Color.Gray)
                Text(
                    text = "Valor por día: $${vehiculo.valor_dia}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00A86B)
                )
            }
        }
    }
}
