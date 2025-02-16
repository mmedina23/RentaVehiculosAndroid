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
import coil.compose.AsyncImage
import com.pmd.rentavehiculos.modelo.Vehiculo
import com.pmd.rentavehiculos.viewModels.AdminViewModel
import com.pmd.rentavehiculos.viewModels.VehiculosViewModel
@Composable
fun RentasAdminScreen(
    apiKey: String,
    vehiculoId: Int,
    adminViewModel: AdminViewModel = viewModel(),
    onBackClick: () -> Unit
) {
    // Llama a la función para obtener el historial de rentas del vehículo
    LaunchedEffect(key1 = vehiculoId) {
        adminViewModel.obtenerHistorialRentas(apiKey, vehiculoId)
    }

    // Observa el LiveData que contiene el historial de rentas
    val rentas by adminViewModel.rentasLiveData.observeAsState(emptyList())
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
            Text(text = "Historial de Renta", style = MaterialTheme.typography.titleLarge)
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
                Text(
                    text = "No hay vehículos rentados",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            else -> {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(rentas) { renta ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                AsyncImage(
                                    model = renta.vehiculo.imagen, // URL de la imagen
                                    contentDescription = "Imagen de ${renta.vehiculo.marca} ${renta.vehiculo.id}",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp)
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                Text("Rentado por: ${renta.persona.nombre} ${renta.persona.apellidos}")
                                Text("Identificación: ${renta.persona.tipo_identificacion} ${renta.persona.identificacion}")
                                Text("Dirección: ${renta.persona.direccion}")
                                Text ("Telefón: ${renta.persona.telefono}")
                                Spacer(modifier = Modifier.height(20.dp))
                                Text("Días: ${renta.dias}")
                                Text("Fecha de Renta: ${renta.fechaRenta}")
                                Text("Fecha Prevista Entrega: ${renta.fechaPrevistaEntrega}")
                                Text("Fecha Entrega: ${renta.fechaEntrega ?: "Pendiente"}")
                                Spacer(modifier = Modifier.height(20.dp))
                                Text("Valor por día: ${renta.vehiculo.valor_dia}")
                                Text("Valor Total: ${renta.valorTotal}")
                            }
                        }
                    }
                }
            }
        }
    }
}
