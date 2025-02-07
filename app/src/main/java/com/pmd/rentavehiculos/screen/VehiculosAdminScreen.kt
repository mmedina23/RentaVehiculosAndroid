package com.pmd.rentavehiculos.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pmd.rentavehiculos.R
import com.pmd.rentavehiculos.model.Vehiculo
import com.pmd.rentavehiculos.viewmodel.LoginViewModel
import com.pmd.rentavehiculos.viewmodel.VehiculosViewModel
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehiculosAdminScreen(
    navController: NavController,
    vehiculosViewModel: VehiculosViewModel = viewModel(),
    loginViewModel: LoginViewModel = viewModel()
) {
    val apiKey = loginViewModel.apiKey.value
    val vehiculosDisponibles = vehiculosViewModel.vehiculosDisponibles
    var showInsertDialog by remember { mutableStateOf(false) }


    var showEditDialog by remember { mutableStateOf(false) } // Estado para abrir el diálogo de edición
    var vehiculoEditando by remember { mutableStateOf<Vehiculo?>(null) } // Almacenar vehículo a editar

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(apiKey) {
        if (apiKey != null) {
            vehiculosViewModel.obtenerVehiculosDisponibles(apiKey)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Panel de Administrador") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF0077B7),
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showInsertDialog = true },
                containerColor = Color(0xFF0077B7),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Vehículo")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Gestión de Vehículos",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (vehiculosDisponibles.isEmpty()) {
                Text(
                    "No hay vehículos disponibles en este momento.",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            } else {
                LazyColumn {
                    items(vehiculosDisponibles) { vehiculo ->
                        VehiculoAdminCard(
                            vehiculo = vehiculo,
                            onEdit = { vehiculoSeleccionado ->
                                showEditDialog = true
                                vehiculoEditando = vehiculoSeleccionado
                            },
                            onDelete = { vehiculoId ->
                                if (apiKey != null) {
                                    vehiculosViewModel.eliminarVehiculo(apiKey, vehiculoId) { success, mensaje ->
                                        coroutineScope.launch { snackbarHostState.showSnackbar(mensaje) }
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    // Composable para editar vehículo
    if (showEditDialog && vehiculoEditando != null) {
        EditarVehiculoDialog(
            vehiculo = vehiculoEditando!!,
            onDismiss = { showEditDialog = false },
            onSave = { vehiculoActualizado ->
                if (apiKey != null) {
                    vehiculosViewModel.actualizarVehiculo(apiKey, vehiculoActualizado) { success, mensaje ->
                        coroutineScope.launch { snackbarHostState.showSnackbar(mensaje) }
                    }
                }
                showEditDialog = false
            }
        )
    }

    if (showInsertDialog) {
        InsertarVehiculoCard(
            vehiculosViewModel = vehiculosViewModel,
            apiKey = apiKey ?: "",
            onClose = { showInsertDialog = false }
        )
    }
}


@Composable
fun VehiculoAdminCard(
    vehiculo: Vehiculo,
    onEdit: (Vehiculo) -> Unit,
    onDelete: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.coche),
                contentDescription = "Imagen del Vehículo",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "🚗 ${vehiculo.marca} - ${vehiculo.color}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0055B7)
                )

                Text("🔹 Carrocería: ${vehiculo.carroceria}", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.width(6.dp))
                Text("🛑 Plazas: ${vehiculo.plazas}", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.width(6.dp))

                Text("⚙️ Cambios: ${vehiculo.cambios}", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.width(6.dp))

                Text("⛽ Combustible: ${vehiculo.tipo_combustible}", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "💰 Precio/día: ${vehiculo.valor_dia} €",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Green
                )
            }

            Column {
                IconButton(onClick = { onEdit(vehiculo) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color(0xFF0077B7))
                }
                IconButton(onClick = { onDelete(vehiculo.id) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                }
            }


        }
    }
}

@Composable
fun EditarVehiculoDialog(
    vehiculo: Vehiculo,
    onDismiss: () -> Unit,
    onSave: (Vehiculo) -> Unit
) {
    var marca by remember { mutableStateOf(vehiculo.marca) }
    var color by remember { mutableStateOf(vehiculo.color) }
    var carroceria by remember { mutableStateOf(vehiculo.carroceria) }
    var plazas by remember { mutableStateOf(vehiculo.plazas.toString()) }
    var cambios by remember { mutableStateOf(vehiculo.cambios) }
    var tipoCombustible by remember { mutableStateOf(vehiculo.tipo_combustible) }
    var valorDia by remember { mutableStateOf(vehiculo.valor_dia.toString()) }

    AlertDialog(containerColor = Color.White,
        onDismissRequest = onDismiss,
        title = { Text("Editar Vehículo") },
        text = {
            Column {
                OutlinedTextField(value = marca, onValueChange = { marca = it }, label = { Text("Marca") })
                OutlinedTextField(value = color, onValueChange = { color = it }, label = { Text("Color") })
                OutlinedTextField(value = carroceria, onValueChange = { carroceria = it }, label = { Text("Carrocería") })
                OutlinedTextField(value = plazas, onValueChange = { plazas = it }, label = { Text("Plazas") })
                OutlinedTextField(value = cambios, onValueChange = { cambios = it }, label = { Text("Cambios") })
                OutlinedTextField(value = tipoCombustible, onValueChange = { tipoCombustible = it }, label = { Text("Combustible") })
                OutlinedTextField(value = valorDia, onValueChange = { valorDia = it }, label = { Text("Valor/día") })
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(
                    vehiculo.copy(
                        marca = marca,
                        color = color,
                        carroceria = carroceria,
                        plazas = plazas.toInt(),
                        cambios = cambios,
                        tipo_combustible = tipoCombustible,
                        valor_dia = valorDia.toDouble()
                    )
                )
            },colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0077B7), contentColor = Color.White)) {
                Text("Guardar",)
            }
        },
        dismissButton = {
            Button(onClick = onDismiss ,colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0077B7), contentColor = Color.White)) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun InsertarVehiculoCard(
    vehiculosViewModel: VehiculosViewModel,
    apiKey: String,
    onClose: () -> Unit
) {
    var marca by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    var carroceria by remember { mutableStateOf("") }
    var plazas by remember { mutableStateOf("") }
    var cambios by remember { mutableStateOf("") }
    var tipoCombustible by remember { mutableStateOf("") }
    var valorDia by remember { mutableStateOf("") }
    var disponible by remember { mutableStateOf(true) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    AlertDialog(containerColor = Color.White,
        onDismissRequest = { onClose() },
        title = { Text("Añadir Nuevo Vehículo") },
        text = {
            Column() {
                OutlinedTextField(value = marca, onValueChange = { marca = it }, label = { Text("Marca") })
                OutlinedTextField(value = color, onValueChange = { color = it }, label = { Text("Color") })
                OutlinedTextField(value = carroceria, onValueChange = { carroceria = it }, label = { Text("Carrocería") })
                OutlinedTextField(value = plazas, onValueChange = { plazas = it }, label = { Text("Plazas") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                OutlinedTextField(value = cambios, onValueChange = { cambios = it }, label = { Text("Cambios") })
                OutlinedTextField(value = tipoCombustible, onValueChange = { tipoCombustible = it }, label = { Text("Tipo Combustible") })
                OutlinedTextField(value = valorDia, onValueChange = { valorDia = it }, label = { Text("Valor por día") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = disponible, onCheckedChange = { disponible = it } )
                    Text("Disponible")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val nuevoVehiculo = Vehiculo(
                        id = 0,
                        marca = marca,
                        color = color,
                        carroceria = carroceria,
                        plazas = plazas.toIntOrNull() ?: 0,
                        cambios = cambios,
                        tipo_combustible = tipoCombustible,
                        valor_dia = valorDia.toDoubleOrNull() ?: 0.0,
                        disponible = disponible
                    )

                    vehiculosViewModel.crearVehiculo(apiKey, nuevoVehiculo) { success, mensaje ->
                        coroutineScope.launch { snackbarHostState.showSnackbar(mensaje) }
                        if (success) onClose()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0077B7), contentColor = Color.White)
            ) {
                Text("Añadir")
            }
        },
        dismissButton = {
            Button(onClick = { onClose() }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0077B7), contentColor = Color.White)) {
                Text("Cancelar")
            }
        }
    )
}




