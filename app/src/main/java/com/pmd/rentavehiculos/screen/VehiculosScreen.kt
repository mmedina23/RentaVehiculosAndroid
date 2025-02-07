package com.pmd.rentavehiculos.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel // IMPORTANTE
import com.pmd.rentavehiculos.dataModel.Vehiculo
import com.pmd.rentavehiculos.viewModel.VehiculosViewModel

@Composable
fun VehiculosScreen(viewModel: VehiculosViewModel = hiltViewModel()) {
    val vehiculos by viewModel.vehiculos.observeAsState(emptyList())
    val loading by viewModel.loading.observeAsState(false)

    if (loading) {
        CircularProgressIndicator()
    } else {
        LazyColumn {
            items(vehiculos) { vehiculo ->
                VehiculoItem(vehiculo)
            }
        }
    }
}

@Composable
fun VehiculoItem(vehiculo: Vehiculo) {
    Card(modifier = Modifier.padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Marca: ${vehiculo.marca}")
            Text("Modelo: ${vehiculo.modelo}")
            Text("Disponible: ${if (vehiculo.disponible) "Sí" else "No"}")
            Text("Precio por Día: ${vehiculo.precioPorDia}")
        }
    }
}
