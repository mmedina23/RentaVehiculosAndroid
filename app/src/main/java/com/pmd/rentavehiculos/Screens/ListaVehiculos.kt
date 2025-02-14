package com.pmd.rentavehiculos.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pmd.rentavehiculos.modelos.Vehiculo
import com.pmd.rentavehiculos.viewmodels.ListaVehiculosViewModel  // ✅ Importamos el ViewModel correcto
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun listaVehiculos(
    navController: NavHostController,
    viewModel: ListaVehiculosViewModel,  //cambio a viewmodelVehiculos
) {

    val context = LocalContext.current
    val vehiculosDisponibles by viewModel.vehiculosDisponibles.observeAsState(emptyList())
    val errorMessage by viewModel.errorMessage.observeAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.fetchVehiculosDisponibles()
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(it)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (vehiculosDisponibles.isEmpty()) {
                Text("No hay vehículos disponibles", fontSize = 18.sp)
            } else {
                LazyColumn {
                    items(vehiculosDisponibles) { vehiculo ->
                        VehiculoCard(vehiculo, viewModel)
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VehiculoCard(vehiculo: Vehiculo, viewModel: ListaVehiculosViewModel) {
    var diasRenta by remember { mutableStateOf("1") }
    var showDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Imagen del vehículo
            Image(
                painter = painterResource(id = com.pmd.rentavehiculos.R.drawable.vehiculo), // Reemplázalo con una imagen real
                contentDescription = "Imagen del vehículo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Información del vehículo
            Text(
                text = "${vehiculo.marca} ${vehiculo.carroceria}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text("Color: ${vehiculo.color}")
            Text("Plazas: ${vehiculo.plazas}")
            Text("Cambio: ${vehiculo.cambios}")
            Text("Combustible: ${vehiculo.tipo_combustible}")
            Text(
                text = "Valor/día: ${vehiculo.valor_dia}€",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Input para los días de renta
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Días: ")
                OutlinedTextField(
                    value = diasRenta,
                    onValueChange = { if (it.all { c -> c.isDigit() }) diasRenta = it },
                    modifier = Modifier.width(60.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón para rentar el vehículo
            Button(
                onClick = { showDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(imageVector = Icons.Filled.ShoppingCart, contentDescription = "Rentar")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Rentar Vehículo")
            }
        }
    }

    // Diálogo de confirmación para rentar el vehículo
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmar renta") },
            text = {
                Text("¿Quieres rentar este vehículo por $diasRenta días? Total: ${vehiculo.valor_dia * diasRenta.toInt()}€")
            },
            confirmButton = {
                TextButton(onClick = {
                    coroutineScope.launch {
                        // Aquí tengo que llamar a la función de ViewModel para rentar el vehículo
                        // viewModel.rentarVehiculo(vehiculo, diasRenta.toInt())
                        snackbarHostState.showSnackbar("Vehículo rentado con éxito!")
                    }
                    showDialog = false
                }) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

