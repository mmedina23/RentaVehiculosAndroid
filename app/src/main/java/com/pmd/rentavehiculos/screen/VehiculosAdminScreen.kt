package com.pmd.rentavehiculos.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.pmd.rentavehiculos.model.Vehiculo
import com.pmd.rentavehiculos.vista.VistaLogin
import com.pmd.rentavehiculos.vista.VistaVehiculos
import androidx.compose.material3.TopAppBarDefaults



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
                title = { Text("Panel de Administrador") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF0077B7),
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* funcion para añadir vehículo */ },
                containerColor = Color(0xFF0077B7),
                contentColor = Color.White
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
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (vehiculosDisponibles.isEmpty()) {
                Text(
                    "No hay vehículos disponibles en este momento.",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            } else {
                LazyColumn {
                    items(vehiculosDisponibles) { vehiculo ->
                        VehiculoAdminCard(vehiculo, onEdit = { /* para editar */ }, onDelete = { /* Acción de eliminar */ })
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
            .padding(vertical = 6.dp, horizontal = 12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.npc),
                contentDescription = "Imagen del Vehículo",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${vehiculo.marca} - ${vehiculo.color}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "Carrocería: ${vehiculo.carroceria}", fontSize = 14.sp)
                Text(text = "Plazas: ${vehiculo.plazas}", fontSize = 14.sp)
                Text(text = "Cambio: ${vehiculo.cambios}", fontSize = 14.sp)
                Text(text = "Combustible: ${vehiculo.tipo_combustible}", fontSize = 14.sp)
                Text(
                    text = "Valor por día: $${vehiculo.valor_dia}",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0077B7),
                    fontSize = 16.sp
                )
            }

            Column {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color(0xFF0077B7))
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                }
            }
        }
    }
}
