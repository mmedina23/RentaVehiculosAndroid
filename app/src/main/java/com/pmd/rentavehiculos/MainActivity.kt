package com.pmd.rentavehiculos

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items


import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pmd.rentavehiculos.data.model.Vehiculo
import androidx.lifecycle.viewmodel.compose.viewModel


import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel

import com.pmd.rentavehiculos.ui.theme.RentaVehiculosTheme


class MainActivity : ComponentActivity() {

    private val vehiculoViewModel: VehiculoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RentaVehiculosTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    VehiculosScreen()
                }
            }
        }

        }
    }

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
                    VehiculoItem(vehiculo)
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
