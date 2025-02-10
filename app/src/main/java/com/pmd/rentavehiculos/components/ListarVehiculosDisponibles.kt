package com.pmd.rentavehiculos.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.pmd.rentavehiculos.data.model.Vehiculo
import com.pmd.rentavehiculos.viewModel.VehiculoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun VehiculosScreen(viewModel: VehiculoViewModel = viewModel()) {
    val vehiculos = viewModel.vehiculos.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Vehículos Disponibles") })
        }
    ) {
        if (vehiculos.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(vehiculos) { vehiculo ->
                    PersonaScreen()
                }
            }
        }
    }
}

@Composable
fun VehiculoItem(vehiculo: Vehiculo) {
    Card(
        elevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "${vehiculo.marca} - ${vehiculo.carroceria}", style = MaterialTheme.typography.headlineSmall )
            Text(text = "Color: ${vehiculo.color}")
            Text(text = "Combustible: ${vehiculo.tipoCombustible}")
            Text(text = "Precio por día: €${vehiculo.valorDia}")
        }
    }
}