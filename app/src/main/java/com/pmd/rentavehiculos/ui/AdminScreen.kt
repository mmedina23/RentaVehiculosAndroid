package com.pmd.rentavehiculos.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pmd.rentavehiculos.components.VehiculoCard
import com.pmd.rentavehiculos.components.VehiculoItem
import com.pmd.rentavehiculos.components.VehiculoRentadoCard
import com.pmd.rentavehiculos.components.VehiculoRentadoItem
import com.pmd.rentavehiculos.viewmodel.VehiculoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(nombreUsuario: String, llaveApi: String, onLogout: () -> Unit) {
    val viewModel: VehiculoViewModel = viewModel()
    val listaVehiculosDisponibles by viewModel.listaVehiculosDisponibles.collectAsState()
    val listaVehiculosRentados by viewModel.listaVehiculosRentados.collectAsState()

    var selectedTabIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.obtenerVehiculosDisponibles(llaveApi)
        viewModel.obtenerVehiculosRentados(0, llaveApi) // Admin ve todos los rentados
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Bienvenido, $nombreUsuario (Admin)") },
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
                .padding(16.dp)
        ) {
            // 🔹 Agregamos pestañas
            TabRow(selectedTabIndex = selectedTabIndex) {
                Tab(selected = selectedTabIndex == 0, onClick = { selectedTabIndex = 0 }) {
                    Text("Disponibles", modifier = Modifier.padding(16.dp))
                }
                Tab(selected = selectedTabIndex == 1, onClick = { selectedTabIndex = 1 }) {
                    Text("Rentados", modifier = Modifier.padding(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedTabIndex) {
                0 -> {
                    Text("Vehículos Disponibles", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn {
                        items(listaVehiculosDisponibles) { vehiculo ->
                            VehiculoCard(vehiculo, onRentar = { /* El admin no renta vehículos */ })
                        }
                    }
                }

                1 -> {
                    Text("Vehículos Rentados", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn {
                        items(listaVehiculosRentados) { renta ->
                            VehiculoRentadoCard(
                                renta,
                                onLiberar = { /* El admin no libera vehículos */ })
                        }
                    }
                }
            }
        }
    }
}