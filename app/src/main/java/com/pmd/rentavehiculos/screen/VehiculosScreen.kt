package com.pmd.rentavehiculos.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.pmd.rentavehiculos.R
import com.pmd.rentavehiculos.viewmodel.LoginViewModel
import com.pmd.rentavehiculos.viewmodel.VehiculosViewModel
import com.pmd.rentavehiculos.model.Vehiculo
import kotlinx.coroutines.launch



@Composable
fun VehiculoImagen(urlImagen: String) {
    AsyncImage(
        model = urlImagen,
        contentDescription = "Imagen del Vehículo",
        contentScale = ContentScale.Crop, // Ajusta la imagen al contenedor
        modifier = Modifier
            .fillMaxSize() // Ocupa todo el espacio disponible en el Box
            .clip(RoundedCornerShape(16.dp)),
        error = painterResource(id = R.drawable.car), // Imagen si falla
        placeholder = painterResource(id = R.drawable.car) // Imagen mientras carga
    )
}


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
                        text = "Vehículos Disponibles",
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF3A8DFF))
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Brush.verticalGradient(listOf(Color(0xFFE0F7FA), Color(0xFF0086F1))))
        ) {
            LazyColumn(
                modifier = Modifier.padding(16.dp)
            ) {
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
            title = { Text(
                text = "Reservar ${selectedVehiculo?.marca}",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color(0xFF1E88E5) // Azul principal
            )  },
            text = {
                Column {
                    Text("Introduce los días de renta:", fontSize = 16.sp, color = Color.Black)

                    OutlinedTextField(
                        value = diasRenta,
                        onValueChange = { input -> if (input.all { it.isDigit() }) diasRenta = input },
                        label = { Text("Días de renta") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val dias = diasRenta.toIntOrNull() ?: 1
                    val total = selectedVehiculo!!.valor_dia * dias
                    Text("Total a pagar: $${total}", fontWeight = FontWeight.Bold)
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

                }) {
                    Text("Confirmar", fontWeight = FontWeight.Bold ,color = Color(0xFF001F3F))
                }
            },
            dismissButton = { Button(onClick = { showDialog = false }) { Text("Cancelar", fontWeight = FontWeight.Bold, color = Color(0xFF001F3F))  } }
        )
    }
}

@Composable
fun VehiculoCard(vehiculo: Vehiculo, onReservarClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .shadow(12.dp, shape = RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Brush.horizontalGradient(listOf(Color(0xFF007DE8), Color(0xFF45A2EC)))),
                contentAlignment = Alignment.Center
            ) {
                VehiculoImagen(urlImagen = vehiculo.imagen)
            }


            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = vehiculo.marca,
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp,
                    color = Color(0xFF1F61A0)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Color: ${vehiculo.color}",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Plazas: ${vehiculo.plazas}",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Carrocería: ${vehiculo.carroceria}",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )



                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Precio/día: ${vehiculo.valor_dia} €",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    color = Color(0xFFFFC200)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onReservarClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A8DFF)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Reservar",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    "Reservar",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}
