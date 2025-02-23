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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.pmd.rentavehiculos.modelos.Vehiculo
import com.pmd.rentavehiculos.viewmodels.LoginViewModel
import com.pmd.rentavehiculos.viewmodels.VehiculosViewModel  // ✅ Importamos el ViewModel correcto
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun listaVehiculos(
    navController: NavHostController,
    viewModel: VehiculosViewModel,
    loginViewModel: LoginViewModel
) {
    val vehiculosDisponibles by viewModel.vehiculosDisponibles.observeAsState(emptyList())
    val errorMessage by viewModel.errorMessage.observeAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.fetchVehiculosDisponibles()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                if (vehiculosDisponibles.isEmpty()) {
                    Text("No hay vehículos disponibles", fontSize = 18.sp)
                } else {
                    LazyColumn {
                        items(vehiculosDisponibles) { vehiculo ->
                            VehiculoCard(vehiculo, viewModel, loginViewModel)
                        }
                    }
                }
            }

            //BOTON MIS VEHICULOS RENTADOS
            ExtendedFloatingActionButton(
                onClick = { navController.navigate("VehiculosRentados") },
                text = { Text("Mis vehículos rentados") },
                icon = { Icon(imageVector = Icons.Filled.ShoppingCart, contentDescription = "Mis Rentados") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            )
        }
    }
}





@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VehiculoCard(
    vehiculo: Vehiculo,
    viewModel: VehiculosViewModel,
    loginViewModel: LoginViewModel // Se agrega LoginViewModel
) {
    var diasRenta by remember { mutableStateOf("1") }
    var showDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val persona = loginViewModel.usuario.value //Obtener la persona autenticada

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            AsyncImage(
                        model = vehiculo.imagen,
                        contentDescription = "Imagen del vehículo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    )


            Spacer(modifier = Modifier.height(8.dp))

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
                        viewModel.rentarVehiculo(
                            vehiculo = vehiculo,
                            persona = persona!!,  // Paso la persona autenticada
                            diasRenta = diasRenta.toInt(),
                            onResult = { success, message ->
                                coroutineScope.launch {
                                    //snackbarHostState.showSnackbar(message) Da mensaje de error que ya no lo uso
                                }
                            }
                        )
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

