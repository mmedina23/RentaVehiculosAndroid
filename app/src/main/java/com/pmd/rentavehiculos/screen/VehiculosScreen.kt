package com.pmd.rentavehiculos.screen

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pmd.rentavehiculos.vista.VistaLogin
import com.pmd.rentavehiculos.vista.VistaVehiculos
import com.pmd.rentavehiculos.model.Vehiculo
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehiculosScreen(
    navController: NavController,
    vehiculosViewModel: VistaVehiculos = viewModel(),
    loginViewModel: VistaLogin = viewModel()
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
                title = { Text("🚗 DriveGo - Alquiler de Vehículos", fontSize = 22.sp, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF0077B7),
                    titleContentColor = Color.White
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .background(Brush.verticalGradient(listOf(Color(0xFFF3F3F3), Color.White)))
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
        AlquilerDialog(
            vehiculo = selectedVehiculo!!,
            diasRenta = diasRenta,
            onDiasChange = { diasRenta = it },
            onConfirm = {
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
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
fun AlquilerDialog(
    vehiculo: Vehiculo,
    diasRenta: String,
    onDiasChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Alquilar ${vehiculo.marca}") },
        text = {
            Column {
                OutlinedTextField(
                    value = diasRenta,
                    onValueChange = { input ->
                        if (input.all { it.isDigit() }) {
                            onDiasChange(input)
                        }
                    },
                    label = { Text("Días de renta") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                val dias = diasRenta.toIntOrNull() ?: 1
                val total = vehiculo.valor_dia * dias
                Text("💰 Total a pagar: $${total}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00A86B))
            }
        },
        confirmButton = {
            Button(onClick = onConfirm, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43A047))) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43A047))) {
                Text("Cancelar")
            }
        },
        containerColor = Color.White
    )
}

@Composable
fun VehiculoCard(vehiculo: Vehiculo, onReservarClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .shadow(8.dp, shape = RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen del vehículo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = vehiculo.imagen,
                    contentDescription = "Imagen del Vehículo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )

            }

            Spacer(modifier = Modifier.height(12.dp))

            // Nombre del vehículo
            Text(
                text = vehiculo.marca,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color(0xFF0077B7),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Información extra en columnas
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "🎨 Color: ${vehiculo.color}", fontSize = 14.sp, color = Color.Gray)
                Text(text = "🚘 Carrocería: ${vehiculo.carroceria}", fontSize = 14.sp, color = Color.Gray)
                Text(text = "🛑 Plazas: ${vehiculo.plazas}", fontSize = 14.sp, color = Color.Gray)
                Text(text = "⚙️ Cambio: ${vehiculo.cambios}", fontSize = 14.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Precio del vehículo
            Text(
                text = "💰 Precio/día: ${vehiculo.valor_dia} €",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF0077B7)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Botón de alquiler
            Button(
                onClick = { onReservarClick() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0077B7)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.ShoppingCart, contentDescription = "Alquilar", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Alquilar", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}
