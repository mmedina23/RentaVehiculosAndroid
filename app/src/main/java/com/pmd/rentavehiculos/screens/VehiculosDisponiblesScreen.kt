package com.pmd.rentavehiculos.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pmd.rentavehiculos.modelo.Vehiculo
import com.pmd.rentavehiculos.viewModels.VehiculosViewModel

@Composable
fun VehiculosDisponiblesScreen(
    apiKey: String,
    onVehiculoClick: (Vehiculo) -> Unit,
    vehiculosViewModel: VehiculosViewModel = viewModel()
) {
    // Llama a la función para obtener vehículos disponibles al iniciar
    LaunchedEffect(Unit) {
        vehiculosViewModel.obtenerVehiculosDisponibles(apiKey)
    }
    val vehiculos by vehiculosViewModel.vehiculosDisponiblesLiveData.observeAsState(emptyList())
    val errorMessage by vehiculosViewModel.errorLiveData.observeAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        when {
            errorMessage != null -> {
                Text(text = errorMessage!!)
            }
            vehiculos.isEmpty() -> {
                // Mientras se carga, muestra un indicador
                CircularProgressIndicator()
            }
            else -> {
                LazyColumn {
                    items(vehiculos) { vehiculo ->
                        VehiculoItem(vehiculo = vehiculo) {
                            onVehiculoClick(vehiculo)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VehiculoItem(vehiculo: Vehiculo, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Marca: ${vehiculo.marca}")
            Text("Valor por día: ${vehiculo.valor_dia}")
            // Puedes agregar más detalles del vehículo aquí
        }
    }
}