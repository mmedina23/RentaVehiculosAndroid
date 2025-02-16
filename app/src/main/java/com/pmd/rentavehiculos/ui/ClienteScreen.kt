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
import com.pmd.rentavehiculos.components.VehiculoItemConBoton
import com.pmd.rentavehiculos.components.VehiculoRentadoCard
import com.pmd.rentavehiculos.viewmodel.VehiculoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteScreen(nombreUsuario: String, llaveApi: String, idUsuario: Int, onLogout: () -> Unit) {
    val viewModel: VehiculoViewModel = viewModel()
    val listaVehiculosDisponibles by viewModel.listaVehiculosDisponibles.collectAsState()
    val listaVehiculosRentados by viewModel.listaVehiculosRentados.collectAsState()

    var tabIndex by remember { mutableStateOf(0) } //pa controlar la pestaña seleccionada.

    LaunchedEffect(Unit) {
        viewModel.obtenerVehiculosDisponibles(llaveApi)
        viewModel.obtenerVehiculosRentados(idUsuario, llaveApi)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Bienvenido, $nombreUsuario (Cliente)") },
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
            //**LOS MF TABS**
            TabRow(selectedTabIndex = tabIndex) {
                Tab(selected = tabIndex == 0, onClick = { tabIndex = 0 }) {
                    Text("Disponibles", modifier = Modifier.padding(8.dp))
                }
                Tab(selected = tabIndex == 1, onClick = { tabIndex = 1 }) {
                    Text("Rentados", modifier = Modifier.padding(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            //PANTALLA SEGÚN LA PESTAÑA SELECCIONADA
            when (tabIndex) {
                0 -> {
                    if (listaVehiculosDisponibles.isEmpty()) {
                        Text("No hay vehículos disponibles.", modifier = Modifier.padding(16.dp))
                    } else {
                        LazyColumn {
                            items(listaVehiculosDisponibles) { vehiculo ->
                                VehiculoCard(vehiculo, onRentar = { /* Lógica de renta */ })
                            }
                        }
                    }
                }

                1 -> {
                    if (listaVehiculosRentados.isEmpty()) {
                        Text("No has rentado vehículos.", modifier = Modifier.padding(16.dp))
                    } else {
                        LazyColumn {
                            items(listaVehiculosRentados) { renta ->
                                VehiculoRentadoCard(renta, onLiberar = { /* Lógica de liberar */ })
                            }
                        }
                    }
                }
            }
            }
    }
}
