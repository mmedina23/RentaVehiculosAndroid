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
fun RentasAdminScreen(
    apiKey: String,
    adminViewModel: AdminViewModel = viewModel(),
    onBackClick: () -> Unit
) {
    // Lanza la carga de datos al iniciar el composable.
    LaunchedEffect(Unit) {
        adminViewModel.obtenerTodasLasRentas(apiKey)
    }

    // Observa el LiveData del ViewModel.
    val rentas by adminViewModel.rentasLiveData.observeAsState(emptyList())
    val errorMessage by adminViewModel.errorLiveData.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Encabezado personalizado con botón de volver atrás.
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
                text = "Vehículos Rentados",
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
            rentas.isEmpty() -> {
                // Centra el CircularProgressIndicator mientras se carga.
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            else -> {
                LazyColumn {
                    items(rentas) { renta ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Vehículo ID: ${renta.vehiculo.id}")
                                Text("Marca: ${renta.vehiculo.marca}")
                                Text("Rentado por: ${renta.persona.nombre} ${renta.persona.apellidos}")
                                Text("Días de Renta: ${renta.dias}")
                                Text("Fecha de Renta: ${renta.fechaRenta}")
                                Text("Fecha Prevista Entrega: ${renta.fechaPrevistaEntrega}")
                                Text("Fecha Entrega: ${renta.fechaEntrega ?: "Pendiente"}")
                                Text("Valor Total: ${renta.valorTotal}")
                            }
                        }
                    }
                }
            }
        }
    }
}
