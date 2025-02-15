package com.pmd.rentavehiculos.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button

import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pmd.rentavehiculos.modelo.Vehiculo
import com.pmd.rentavehiculos.viewModels.AdminViewModel
import com.pmd.rentavehiculos.viewModels.VehiculosViewModel

@Composable
fun VehiculosDisponiblesAdminScreen(
    apiKey: String,
    adminViewModel: AdminViewModel = viewModel(),
    onBackClick: () -> Unit,
    onVehiculoClick: (Vehiculo) -> Unit
) {
    val vehiculosViewModel: VehiculosViewModel = viewModel()

    LaunchedEffect(Unit) {
        vehiculosViewModel.obtenerVehiculosDisponibles(apiKey)
    }
    val vehiculos by vehiculosViewModel.vehiculosDisponiblesLiveData.observeAsState(emptyList());
    val errorMessage by adminViewModel.errorLiveData.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Encabezado personalizado con botón de volver atrás
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver atrás"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Vehículos Disponibles",
                style = MaterialTheme.typography.titleLarge
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            errorMessage != null -> {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            vehiculos.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            else -> {
                LazyColumn (modifier = Modifier.weight(1f)) {
                    items(vehiculos) { vehiculo ->
                        // Usa el componente VehiculoItemAdmin
                        VehiculoItemAdmin(vehiculo = vehiculo) {
                            onVehiculoClick(vehiculo)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun VehiculoItemAdmin(vehiculo: Vehiculo, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                Log.d("VehiculoItemAdmin", "Se hizo click en el vehículo con id: ${vehiculo.id}")
                onClick()
            }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "ID: ${vehiculo.id}")
            Text(text = "Marca: ${vehiculo.marca}")
            Text(text = "Carrocería: ${vehiculo.carroceria}")
            Text(text = "Plazas: ${vehiculo.plazas}")
            Text(text = "Cambios: ${vehiculo.cambios}")
            Text(text = "Valor por día: ${vehiculo.valor_dia}")
        }
    }
}
