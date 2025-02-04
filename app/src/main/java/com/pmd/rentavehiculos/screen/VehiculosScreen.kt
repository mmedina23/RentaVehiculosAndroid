package com.pmd.rentavehiculos.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pmd.rentavehiculos.viewmodel.LoginViewModel
import com.pmd.rentavehiculos.viewmodel.VehiculosViewModel
import com.pmd.rentavehiculos.model.Vehiculo
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehiculosScreen(
    navController: NavController,
    vehiculosViewModel: VehiculosViewModel = viewModel(),
    loginViewModel: LoginViewModel = viewModel()
) {
    val apiKey = loginViewModel.apiKey.value
    val usuario = loginViewModel.usuario.value
    val vehiculos = vehiculosViewModel.vehiculos
    var selectedVehiculo by remember { mutableStateOf<Vehiculo?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var diasRenta by remember { mutableStateOf("1") }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(apiKey) {
        if (apiKey != null) {
            vehiculosViewModel.obtenerVehiculos(apiKey)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Vehículos Para Alquilar",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0077B7))
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            LazyColumn {
                items(vehiculos) { vehiculo ->
                    VehiculoCard(vehiculo) {
                        selectedVehiculo = vehiculo
                        showDialog = true
                    }
                }
            }
        }
    }

    if (showDialog && selectedVehiculo != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Reservar ${selectedVehiculo?.marca}") },
            text = {
                Column {
                    Text("Introduce los días de renta:")

                    OutlinedTextField(
                        value = diasRenta,
                        onValueChange = { input ->
                            if (input.all { it.isDigit() }) {
                                diasRenta = input
                            }
                        },
                        label = { Text("Días de renta") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val dias = diasRenta.toIntOrNull() ?: 1
                    val total = selectedVehiculo!!.valor_dia * dias
                    Text("Total a pagar: $${total}")
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (apiKey != null && usuario != null) {
                        val dias = diasRenta.toIntOrNull() ?: 1
                        vehiculosViewModel.reservarVehiculo(
                            apiKey,
                            usuario,
                            selectedVehiculo!!,
                            dias
                        ) { success, message ->
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(message)
                            }
                            showDialog = false
                        }
                    }
                },
                        colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0077B7),
                    contentColor = Color.White)) {
                    Text("Confirmar")
                }
            },
            dismissButton = { Button(onClick = { showDialog = false },colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0077B7),
                contentColor = Color.White)) { Text("Cancelar") } },
            containerColor = Color.White

        )
    }

}


@Composable
fun VehiculoCard(vehiculo: Vehiculo, onReservarClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .shadow(8.dp, shape = RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp) //
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                    contentDescription = "Imagen del Vehículo",
                    modifier = Modifier.size(100.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = vehiculo.marca,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color(0xFF0055B7)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Color: ${vehiculo.color}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Plazas: ${vehiculo.plazas}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Carrocería: ${vehiculo.carroceria}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Cambios: ${vehiculo.cambios}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Precio/día: ${vehiculo.valor_dia} €",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = Color.Red
                )
            }

            Spacer(modifier = Modifier.height(12.dp))


            Button(
                onClick = { onReservarClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0077B7)), // Naranja llamativo
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Reservar",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Reservar",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}




