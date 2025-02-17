package com.pmd.rentavehiculos.pantallas

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pmd.rentavehiculos.modelo.Vehiculo
import com.pmd.rentavehiculos.viewModels.LoginViewModel
import com.pmd.rentavehiculos.viewModels.VehiculosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehiculosScreen(
    navController: NavController,
    vehiculosViewModel: VehiculosViewModel = viewModel(),
    loginViewModel: LoginViewModel = viewModel()
) {
    val apiKey = loginViewModel.apiKey.value
    loginViewModel.usuario.value
    val vehiculos = vehiculosViewModel.vehiculos

    var selectedVehiculo by remember { mutableStateOf<Vehiculo?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    rememberCoroutineScope()

    // Estado para almacenar la selección del tipo de combustible
    var tipoCombustibleSeleccionado by remember { mutableStateOf("TODOS") }

    // Obtener la lista única de tipos de combustible
    val tiposCombustible = listOf("TODOS") + vehiculos.map { it.tipo_combustible }.distinct()

    // Filtrar los vehículos según el tipo de combustible seleccionado
    val vehiculosFiltrados = if (tipoCombustibleSeleccionado == "TODOS") {
        vehiculos
    } else {
        vehiculos.filter { it.tipo_combustible == tipoCombustibleSeleccionado }
    }

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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF800080)) // Morado fuerte
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
            // Selector de Tipo de Combustible con RadioButtons en fila
            Text(
                text = "Filtrar por tipo de combustible:",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFF9932CC), RoundedCornerShape(8.dp)), // Morado claro
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                tiposCombustible.forEach { tipo ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .selectable(
                                selected = (tipo == tipoCombustibleSeleccionado),
                                onClick = { tipoCombustibleSeleccionado = tipo }
                            )
                            .padding(8.dp),
                    ) {
                        RadioButton(
                            selected = (tipo == tipoCombustibleSeleccionado),
                            onClick = { tipoCombustibleSeleccionado = tipo },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFF800080) // Morado fuerte
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = tipo, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(vehiculosFiltrados) { vehiculo ->
                    VehiculoCard(vehiculo) {
                        selectedVehiculo = vehiculo
                        showDialog = true
                    }
                }
            }
        }
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
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = vehiculo.marca,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color(0xFF800080) // Morado fuerte
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Color: ${vehiculo.color}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = "Plazas: ${vehiculo.plazas}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = "Carrocería: ${vehiculo.carroceria}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = "Cambios: ${vehiculo.cambios}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = "Combustible: ${vehiculo.tipo_combustible}", fontWeight = FontWeight.Bold, fontSize = 16.sp)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${vehiculo.valor_dia} €/día",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = Color.Red
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Botón de Reservar
            Button(
                onClick = { onReservarClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF800080)), // Morado fuerte
                shape = RoundedCornerShape(8.dp)
            ) {
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
