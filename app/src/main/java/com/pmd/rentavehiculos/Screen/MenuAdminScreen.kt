package com.pmd.rentavehiculos.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pmd.rentavehiculos.model.Vehiculo
import com.pmd.rentavehiculos.viewmodel.LoginViewModel
import com.pmd.rentavehiculos.viewmodel.VehiculosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuAdminScreen(navController: NavController, loginViewModel: LoginViewModel = viewModel()) {
    var isDialogOpen by remember { mutableStateOf(false) }
    var vehiculoId by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Panel de Administrador", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF0D47A1),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // 🔥 Imagen eliminada

            Text(
                text = "Bienvenido, Administrador",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D47A1),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Button(
                onClick = { navController.navigate("vehiculos_disponibles") },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0077B7)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Text("Gestión de Vehículos", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { isDialogOpen = true },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0077B7)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ver Historial de Rentas", fontSize = 16.sp)
            }

            if (isDialogOpen) {
                AlertDialog(
                    onDismissRequest = { isDialogOpen = false },
                    title = { Text("Buscar Historial de Rentas") },
                    text = {
                        Column {
                            Text("Ingrese el ID del vehículo:")
                            OutlinedTextField(
                                value = vehiculoId,
                                onValueChange = { vehiculoId = it },
                                singleLine = true
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                isDialogOpen = false
                                if (vehiculoId.isNotEmpty()) {
                                    navController.navigate("vehiculos_rentados_admin/$vehiculoId")
                                }
                            }
                        ) {
                            Text("Buscar")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { isDialogOpen = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}
