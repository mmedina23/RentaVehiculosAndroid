@file:OptIn(ExperimentalMaterial3Api::class)

package com.pmd.rentavehiculos.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pmd.rentavehiculos.viewmodel.VehiculoViewModel

@Composable
fun VehiculoScreen(llaveApi: String, nombreUsuario: String, onLogout: () -> Unit) {
    val viewModel: VehiculoViewModel = viewModel()
    val listaVehiculos by viewModel.listaVehiculos.collectAsState()

    val usuarioActualizado by rememberUpdatedState(nombreUsuario) // 🔥 Asegurar actualización

    LaunchedEffect(Unit) {
        viewModel.obtenerVehiculos(llaveApi)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Bienvenido, $usuarioActualizado") }, // 🔥 Se actualiza correctamente
                actions = {
                    Button(onClick = onLogout) {
                        Text("Cerrar Sesión")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = "Lista de Vehículos", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))

            listaVehiculos.forEach { vehiculo ->
                Text(text = "🚗 Marca: ${vehiculo.marca}, Carrocería: ${vehiculo.carroceria}")
            }
        }
    }
}


