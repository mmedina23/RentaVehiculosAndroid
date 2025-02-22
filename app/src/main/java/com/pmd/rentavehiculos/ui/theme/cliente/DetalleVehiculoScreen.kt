package com.pmd.rentavehiculos.ui.theme.cliente

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pmd.rentavehiculos.ui.theme.viewmodel.ClienteViewModel
import kotlinx.coroutines.launch

@Composable
fun DetalleVehiculoScreen(
    vehiculoId: String,
    viewModel: ClienteViewModel,
    navController: NavController
) {
    val vehiculoIdInt = vehiculoId.toIntOrNull()
    if (vehiculoIdInt == null) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
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
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        vehiculoDetalle?.let { vehiculo ->
            Card(
                modifier = Modifier.padding(16.dp),
                border = BorderStroke(2.dp, Color.White),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
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
                    Button(onClick = { showDialog = true },colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),) {
                        Text("Rentar Vehículo", color = Color.White)
                    }
                    errorMessage?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(it, color = Color.Red)
                    }
                }
            }
        } ?: CircularProgressIndicator()
    }

    if (showDialog) {
        LaunchedEffect(showDialog) {
            val dias = diasRenta.toIntOrNull()?.coerceAtLeast(1) ?: 1
            totalCosto = dias * valorPorDia
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Card(
                modifier = Modifier.padding(16.dp),
                border = BorderStroke(2.dp, Color.White),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xAA000000))
            ) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Seleccionar días de renta", color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = diasRenta,
                        onValueChange = { input ->
                            if (input.all { it.isDigit() }) {
                                diasRenta = input
                                val dias = input.toIntOrNull()?.coerceAtLeast(1) ?: 1
                                totalCosto = dias * valorPorDia
                            }
                        },
                        label = { Text("Días", color = Color.White) },
                        singleLine = true
                    )
                    Text("Costo Total: $${"%.2f".format(totalCosto)}", color = Color.White)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row {
                        Button(
                            onClick = {
                                val dias = diasRenta.toIntOrNull()?.coerceAtLeast(1) ?: 1
                                scope.launch {
                                    viewModel.rentarVehiculo(vehiculoDetalle!!, dias) { success, message ->
                                        if (success) {
                                            navController.navigate("rentas_actuales") {
                                                popUpTo("cliente_home") { inclusive = false }
                                            }
                                        } else {
                                            errorMessage = message
                                        }
                                    }
                                }
                                showDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                            enabled = diasRenta.isNotEmpty()
                        ) {
                            Text("Confirmar", color = Color.White)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = { showDialog = false },colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),) {
                            Text("Cancelar", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
