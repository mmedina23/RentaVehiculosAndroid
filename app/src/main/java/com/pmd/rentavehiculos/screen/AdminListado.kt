package com.pmd.rentavehiculos.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pmd.rentavehiculos.dataModel.Vehiculo
import com.pmd.rentavehiculos.viewModel.VehiculosViewModel
import com.pmd.rentavehiculos.viewModel.VehiculosViewModelFactory

@Composable
fun AdminListado() {
    val vehiculosViewModel: VehiculosViewModel = viewModel(
        factory = VehiculosViewModelFactory(LocalContext.current)
    )
    val vehiculos = vehiculosViewModel.vehiculos.observeAsState(emptyList())
    val isLoading = vehiculosViewModel.loading.observeAsState(false)


    // Fetch data when the screen is loaded
    LaunchedEffect(Unit) {
        vehiculosViewModel.fetchVehiculos()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading.value) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(vehiculos.value) { vehiculo ->
                    VehiculoItem(vehiculo)
                }
            }
        }
    }
}

@Composable
fun VehiculoItem(vehiculo: Vehiculo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Marca: ${vehiculo.marca}", style = MaterialTheme.typography.bodyLarge)
            Text(
                text = "Modelo: ${vehiculo.carroceria}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(text = "Color: ${vehiculo.color}", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "Precio por día: ${vehiculo.valor_dia}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
