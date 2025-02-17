package com.pmd.rentavehiculos.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pmd.rentavehiculos.R
import com.pmd.rentavehiculos.modelo.Vehiculo
import com.pmd.rentavehiculos.viewModels.LoginViewModel
import com.pmd.rentavehiculos.viewModels.VehiculosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehiculosAdminScreen(
    navController: NavController,
    vehiculosViewModel: VehiculosViewModel = viewModel(),
    loginViewModel: LoginViewModel = viewModel()
) {
    val apiKey = loginViewModel.apiKey.value
    val vehiculosDisponibles = vehiculosViewModel.vehiculosDisponibles

    // Cargar la lista de vehículos disponibles al iniciar la pantalla
    LaunchedEffect(apiKey) {
        if (apiKey != null) {
            vehiculosViewModel.obtenerVehiculosDisponibles(apiKey)
        }
    }

    Scaffold(
        // Barra superior con color morado fuerte
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Panel de Administrador") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF800080),  // Morado fuerte
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
            // 🔹 Título de la pantalla
            Text(
                text = "Gestión de Vehículos",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF9932CC),  // Morado claro
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // 🔹 Mostrar mensaje si no hay vehículos disponibles
            if (vehiculosDisponibles.isEmpty()) {
                Text(
                    text = "No hay vehículos disponibles en este momento.",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF4B0082)  // Morado intermedio
                )
            } else {
                // 🔹 Lista de vehículos disponibles
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
            .padding(vertical = 6.dp, horizontal = 12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 🔹 Imagen representativa del vehículo
            Image(
                painter = painterResource(id = R.drawable.logo_playstore),
                contentDescription = "Imagen del Vehículo",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                // 🔹 Datos del vehículo
                Text(
                    text = "${vehiculo.marca} - ${vehiculo.color}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF800080)  // Morado fuerte
                )

                Text("Carrocería: ${vehiculo.carroceria}", fontSize = 14.sp, color = Color.Gray)
                Text("Plazas: ${vehiculo.plazas}", fontSize = 14.sp, color = Color.Gray)
                Text("Cambios: ${vehiculo.cambios}", fontSize = 14.sp, color = Color.Gray)
                Text("Combustible: ${vehiculo.tipo_combustible}", fontSize = 14.sp, color = Color.Gray)

                // 🔹 Precio del vehículo
                Text(
                    text = "Precio/día: ${vehiculo.valor_dia} €",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF9932CC)  // Morado claro
                )
            }
        }
    }
}
