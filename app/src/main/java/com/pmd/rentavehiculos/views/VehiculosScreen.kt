package com.pmd.rentavehiculos.views

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.pmd.rentavehiculos.models.Vehiculo
import com.pmd.rentavehiculos.viewmodels.VehiculoViewModel

class VehiculosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VehiculosScreen(
                viewModel = TODO()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehiculosScreen(viewModel: VehiculoViewModel) {
    val context = LocalContext.current

    LaunchedEffect(Unit) { viewModel.cargarVehiculos() }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Vehículos Disponibles") }) }
    ) { padding ->
        if (viewModel.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(viewModel.vehiculos) { vehiculo ->
                    VehiculoItem(vehiculo) { dias ->
                        viewModel.rentarVehiculo(1, vehiculo.id, dias) { mensaje ->
                            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VehiculoItem(vehiculo: Vehiculo, onRentar: (Int) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    var dias by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(text = "${vehiculo.marca} ${vehiculo.modelo}", style = MaterialTheme.typography.titleMedium)
                    Text(text = "Precio por día: \$${vehiculo.precioPorDia}", style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = { showDialog = true }) {
                Text("Rentar")
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Rentar Vehículo") },
                    text = {
                        Column {
                            Text("Ingrese la cantidad de días:")
                            OutlinedTextField(
                                value = dias,
                                onValueChange = { dias = it },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    confirmButton = {
                        Button(onClick = {
                            val diasInt = dias.toIntOrNull()
                            if (diasInt != null && diasInt > 0) {
                                onRentar(diasInt)
                                showDialog = false
                            }
                        }) {
                            Text("Confirmar")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showDialog = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}
