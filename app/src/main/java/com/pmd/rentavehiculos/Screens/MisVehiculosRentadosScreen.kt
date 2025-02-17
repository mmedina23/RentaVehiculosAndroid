package com.pmd.rentavehiculos.Screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pmd.rentavehiculos.R
import com.pmd.rentavehiculos.modelos.Renta
import com.pmd.rentavehiculos.viewmodels.VehiculosViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VehiculosRentadosScreen(
    navController: NavHostController,
    viewModel: VehiculosViewModel
) {
    val vehiculosRentados by viewModel.vehiculosRentados.observeAsState(emptyList())
    val errorMessage by viewModel.errorMessage.observeAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.fetchVehiculosRentados()
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
            Text(
                text = "Vehículos Rentados",
                fontSize = 22.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            )

            if (vehiculosRentados.isEmpty()) {
                Text(
                    text = "No tienes vehículos rentados",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )
            } else {
                LazyColumn {
                    items(vehiculosRentados) { renta ->
                        VehiculoRentadoCard(renta, viewModel, snackbarHostState)
                    }
                }
            }
        }
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(it)
            }
        }
    }
}

@Composable
fun VehiculoRentadoCard(renta: Renta, viewModel: VehiculosViewModel, snackbarHostState: SnackbarHostState) {
    var showDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(id = R.drawable.vehiculo),
                contentDescription = "Imagen del vehículo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${renta.vehiculo.marca} ${renta.vehiculo.carroceria}",
                fontSize = 20.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
            Text("Color: ${renta.vehiculo.color}")
            Text("Plazas: ${renta.vehiculo.plazas}")
            Text("Cambio: ${renta.vehiculo.cambios}")
            Text("Combustible: ${renta.vehiculo.tipo_combustible}")
            Text(
                "Valor/día: ${renta.vehiculo.valor_dia}€",
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                fontSize = 18.sp
            )
            Text("Días rentados: ${renta.dias_renta}")
            Text("Total pagado: ${renta.valor_total_renta}€")
            Text("Fecha de renta: ${renta.fecha_renta}")
            Text("Entrega estimada: ${renta.fecha_estimada_entrega}")

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { showDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(imageVector = Icons.Filled.Delete, contentDescription = "Liberar vehículo")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Liberar Vehículo")
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmar Liberación") },
            text = { Text("¿Estás seguro de que quieres liberar este vehículo?") },
            confirmButton = {
                TextButton(onClick = {
                    coroutineScope.launch {
                        viewModel.liberarVehiculo(renta.vehiculo.id) { success, message ->
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(message)
                            }
                        }
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
