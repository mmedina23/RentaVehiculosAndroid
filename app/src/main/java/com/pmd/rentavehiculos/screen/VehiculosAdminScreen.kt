package com.pmd.rentavehiculos.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pmd.rentavehiculos.model.Vehiculo
import com.pmd.rentavehiculos.viewmodel.LoginViewModel
import com.pmd.rentavehiculos.viewmodel.VehiculosViewModel
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehiculosAdminScreen(
    navController: NavController,
    vehiculosViewModel: VehiculosViewModel = viewModel(),
    loginViewModel: LoginViewModel = viewModel()
) {
    val apiKey = loginViewModel.apiKey.value
    val vehiculosDisponibles = vehiculosViewModel.vehiculosDisponibles

    LaunchedEffect(apiKey) {
        if (apiKey != null) {
            try {
                vehiculosViewModel.obtenerVehiculosDisponibles(apiKey)
            } catch (e: Exception) {
                Log.e("VehiculosAdminScreen", "Error al obtener vehículos", e)
            }
        } else {
            Log.e("VehiculosAdminScreen", "API Key es nula.")
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Administración de Vehículos", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF1E3A8A),
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("añadirVehiculo") }, // ✅ Navegar a pantalla de añadir
                containerColor = Color(0xFF34D399),
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Vehículo")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Gestión de Vehículos",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp),
                color = Color(0xFF1E3A8A)
            )

            if (vehiculosDisponibles.isEmpty()) {
                Text(
                    "No hay vehículos disponibles en este momento.",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
            } else {
                LazyColumn {
                    items(vehiculosDisponibles) { vehiculo ->
                        VehiculoAdminCard(
                            vehiculo,
                            onEdit = { navController.navigate("editarVehiculo/${vehiculo.id}") }, // ✅ Editar con ID
                            onDelete = {
                                apiKey?.let { vehiculosViewModel.eliminarVehiculo(vehiculo.id, it) }
                            } // ✅ Eliminar con API Key
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VehiculoAdminCard(vehiculo: Vehiculo, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp)
            .clip(RoundedCornerShape(16.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(Color.Gray, RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
            ) {
                // Imagen de placeholder
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${vehiculo.marca} - ${vehiculo.color}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E3A8A)
                )
                Text(text = "Carrocería: ${vehiculo.carroceria}", fontSize = 14.sp, color = Color.DarkGray)
                Text(text = "Plazas: ${vehiculo.plazas}", fontSize = 14.sp, color = Color.DarkGray)
                Text(text = "Cambio: ${vehiculo.cambios}", fontSize = 14.sp, color = Color.DarkGray)
                Text(text = "Combustible: ${vehiculo.tipo_combustible}", fontSize = 14.sp, color = Color.DarkGray)
                Text(
                    text = "Valor por día: $${vehiculo.valor_dia}",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF34D399),
                    fontSize = 16.sp
                )
            }

            Column {
                IconButton(onClick = onEdit) { // ✅ Editar vehículo
                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color(0xFF34D399))
                }
                IconButton(onClick = onDelete) { // ✅ Eliminar vehículo
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color(0xFFE53935))
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewVehiculosAdminScreen() {
    VehiculosAdminScreen(navController = rememberNavController())
}
