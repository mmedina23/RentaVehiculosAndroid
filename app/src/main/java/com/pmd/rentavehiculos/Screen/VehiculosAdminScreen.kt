package com.pmd.rentavehiculos.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pmd.rentavehiculos.viewmodel.LoginViewModel
import com.pmd.rentavehiculos.viewmodel.VehiculosViewModel
import com.pmd.rentavehiculos.model.Vehiculo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehiculosAdminScreen(
    navController: NavController,
    vehiculosViewModel: VehiculosViewModel = viewModel(),
    loginViewModel: LoginViewModel = viewModel()
) {
    val apiKey = loginViewModel.apiKey.value
    val vehiculosDisponibles by vehiculosViewModel.vehiculosDisponibles.collectAsState()

    // Estado del filtro
    var filtroSeleccionado by remember { mutableStateOf("Todos") }

    LaunchedEffect(apiKey) {
        apiKey?.let { vehiculosViewModel.obtenerVehiculosDisponibles(it) }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Gestión de Vehículos") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Menú desplegable para filtrar
            FiltroVehiculosDropdown(filtroSeleccionado) { nuevoFiltro ->
                filtroSeleccionado = nuevoFiltro
            }

            Spacer(modifier = Modifier.height(16.dp))

            val vehiculosFiltrados = filtrarVehiculos(vehiculosDisponibles, filtroSeleccionado)

            if (vehiculosFiltrados.isEmpty()) {
                Text("No hay vehículos disponibles para este filtro.")
            } else {
                LazyColumn {
                    items(vehiculosFiltrados) { vehiculo ->
                        VehiculoCardAdmin(vehiculo)
                    }
                }
            }
        }
    }
}

// Función para mostrar el DropdownMenu
@Composable
fun FiltroVehiculosDropdown(filtroSeleccionado: String, onFiltroSeleccionado: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Button(onClick = { expanded = true }) {
            Icon(Icons.Default.FilterList, contentDescription = "Filtrar")
            Spacer(modifier = Modifier.width(8.dp))
            Text(filtroSeleccionado)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listOf("Todos", "Disponibles", "No Disponibles", "Gasolina", "Diésel", "Eléctrico").forEach { filtro ->
                DropdownMenuItem(
                    text = { Text(filtro) },
                    onClick = {
                        onFiltroSeleccionado(filtro)
                        expanded = false
                    }
                )
            }
        }
    }
}

// Función para filtrar la lista de vehículos
fun filtrarVehiculos(vehiculos: List<Vehiculo>, filtro: String): List<Vehiculo> {
    return when (filtro) {
        "Disponibles" -> vehiculos.filter { it.disponible }
        "No Disponibles" -> vehiculos.filter { !it.disponible }
        "Gasolina" -> vehiculos.filter { it.tipo_combustible.equals("GASOLINA", ignoreCase = true) }
        "Diésel" -> vehiculos.filter { it.tipo_combustible.equals("DIESEL", ignoreCase = true) }
        "Eléctrico" -> vehiculos.filter { it.tipo_combustible.equals("ELECTRICO", ignoreCase = true) }
        else -> vehiculos
    }
}

// Card para mostrar la información de los vehículos
@Composable
fun VehiculoCardAdmin(vehiculo: Vehiculo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Marca: ${vehiculo.marca}")
            Text(text = "Modelo: ${vehiculo.carroceria}")
            Text(text = "Plazas: ${vehiculo.plazas}")
            Text(text = "Cambio: ${vehiculo.cambios}")
            Text(text = "Combustible: ${vehiculo.tipo_combustible}")
            Text(text = "Disponible: ${if (vehiculo.disponible) "Sí" else "No"}")
        }
    }
}
