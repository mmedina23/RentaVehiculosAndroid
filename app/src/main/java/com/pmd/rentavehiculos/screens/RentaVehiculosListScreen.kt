package com.pmd.rentavehiculos.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pmd.rentavehiculos.viewModels.AdminViewModel
import androidx.compose.runtime.getValue
import com.pmd.rentavehiculos.modelo.Vehiculo


@Composable
fun RentaVehiculosListScreen(
    apiKey: String,
    adminViewModel: AdminViewModel = viewModel(),
    onVehicleClick: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    // Llama a la función para obtener vehículos rentados al iniciar
    LaunchedEffect(Unit) {
        adminViewModel.obtenerVehiculosRentadosAdmin(apiKey)
    }

    // Observa el LiveData que contendrá la lista de vehículos rentados
    val vehiculosRentados by adminViewModel.vehiculosRentadosAdminLiveData.observeAsState(emptyList())
    val errorMessage by adminViewModel.errorLiveData.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Encabezado con botón de volver
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBackClick) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Vehículos Rentados", style = MaterialTheme.typography.titleLarge)
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
            vehiculosRentados.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            else -> {
                LazyColumn (modifier = Modifier.weight(1f)) {
                    items<Vehiculo>(vehiculosRentados) { vehiculo ->                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable { onVehicleClick(vehiculo.id) }
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("ID: ${vehiculo.id}")
                                Text("Marca: ${vehiculo.marca}")
                                // Puedes agregar más datos si lo consideras
                            }
                        }
                    }
                }
            }
        }
    }
}