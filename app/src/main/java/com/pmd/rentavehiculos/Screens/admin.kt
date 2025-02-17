package com.pmd.rentavehiculos.ui.admin

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.pmd.rentavehiculos.modelos.Vehiculo
import com.pmd.rentavehiculos.viewmodels.VehiculosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun admin( //vision admin de los vehiculos disponibles
    navController: NavHostController,
    viewModel: VehiculosViewModel
) {
    val vehiculosDisponibles by viewModel.vehiculosDisponibles.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.fetchVehiculosDisponibles()
    }

    Scaffold(
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Lista de Vehículos Disponibles",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (vehiculosDisponibles.isEmpty()) {
                Text("No hay vehículos disponibles", fontSize = 18.sp)
            } else {
                LazyColumn {
                    items(vehiculosDisponibles) { vehiculo ->
                        VehiculoDisponibleCard(vehiculo)
                    }
                }
            }
        }
    }
}


@Composable
fun VehiculoDisponibleCard(vehiculo: Vehiculo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = vehiculo.imagen,
                contentDescription = "Imagen del vehículo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${vehiculo.marca} ${vehiculo.carroceria}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

            Text("Color: ${vehiculo.color}")
            Text("Plazas: ${vehiculo.plazas}")
            Text("Cambio: ${vehiculo.cambios}")
            Text("Combustible: ${vehiculo.tipo_combustible}")
            Text(
                text = "Valor/día: ${vehiculo.valor_dia}€",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Estado: Disponible",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

