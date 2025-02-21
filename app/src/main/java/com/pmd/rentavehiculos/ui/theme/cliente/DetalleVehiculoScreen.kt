package com.pmd.rentavehiculos.ui.theme.cliente

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pmd.rentavehiculos.ui.theme.viewmodel.ClienteViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleVehiculoScreen(
    vehiculoId: String,
    viewModel: ClienteViewModel,
    navController: NavController
) {
    val vehiculoIdInt = vehiculoId.toIntOrNull()
    if (vehiculoIdInt == null) {
        // Si no se puede convertir a Int, muestra un mensaje de error
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("Error: ID de vehículo inválido", color = Color.Red)
            Button(onClick = { navController.popBackStack() }) {
                Text("Regresar")
            }
        }
        return
    }
    val vehiculoDetalle by viewModel.obtenerDetalleVehiculo(vehiculoIdInt).collectAsState(initial = null)
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var diasRenta by remember { mutableStateOf("1") }
    val valorPorDia = vehiculoDetalle?.valor_dia ?: 0.0
    var totalCosto by remember { mutableStateOf(valorPorDia) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalles del Vehículo") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { paddingValues ->
        vehiculoDetalle?.let { vehiculo ->
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                AsyncImage(
                    model = vehiculo.imagen,
                    contentDescription = "Imagen del vehículo",
                    modifier = Modifier.fillMaxWidth().height(250.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Marca: ${vehiculo.marca}", style = MaterialTheme.typography.bodyLarge)
                Text("Modelo: ${vehiculo.carroceria}")
                Text("Color: ${vehiculo.color}")
                Text("Plazas: ${vehiculo.plazas}")
                Text("Tipo Combustible: ${vehiculo.tipo_combustible}")
                Text("Valor por Día: $${"%.2f".format(vehiculo.valor_dia)}")

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { showDialog = true }) {
                    Text("Rentar Vehículo")
                }
            }
        } ?: CircularProgressIndicator()
    }

    if (showDialog) {
        // 🔹 Recalcular el costo total al abrir el diálogo
        LaunchedEffect(showDialog) {
            val dias = diasRenta.toIntOrNull()?.coerceAtLeast(1) ?: 1
            totalCosto = dias * valorPorDia
        }

        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Seleccionar días de renta") },
            text = {
                Column {
                    Text("Ingrese la cantidad de días:")
                    OutlinedTextField(
                        value = diasRenta,
                        onValueChange = { input ->
                            if (input.all { it.isDigit() }) {
                                diasRenta = input
                                val dias = input.toIntOrNull()?.coerceAtLeast(1) ?: 1
                                totalCosto = dias * valorPorDia
                            }
                        },
                        label = { Text("Días") },
                        singleLine = true
                    )
                    Text("Costo Total: $${"%.2f".format(totalCosto)}")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val dias = diasRenta.toIntOrNull()?.coerceAtLeast(1) ?: 1
                        scope.launch {
                            viewModel.rentarVehiculo(vehiculoDetalle!!, dias) { success, message ->
                                if (success) {
                                    navController.navigate("rentas_actuales") {
                                        popUpTo(navController.graph.startDestinationId) { inclusive = false }
                                    }
                                }
                            }
                        }
                        showDialog = false
                    },
                    enabled = diasRenta.isNotEmpty()
                ) {
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
