package com.pmd.rentavehiculos.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pmd.rentavehiculos.R
import com.pmd.rentavehiculos.viewModels.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuAdminScreen(navController: NavController, loginViewModel: LoginViewModel = viewModel()) {
    var isDialogOpen by remember { mutableStateOf(false) } // Estado para abrir/cerrar el cuadro de diálogo
    var vehiculoId by remember { mutableStateOf("") } // Estado para almacenar el ID del vehículo

    Scaffold(
        // Barra superior con título "Panel de Administrador"
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Panel de Administrador",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF800080), // Morado fuerte para la barra superior
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
            // Imagen del administrador
            Image(
                painter = painterResource(id = R.drawable.logo_playstore),
                contentDescription = "Imagen de Administrador",
                modifier = Modifier
                    .size(300.dp)
                    .padding(bottom = 16.dp)
            )

            // Texto de bienvenida
            Text(
                text = "Bienvenido, Administrador",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4B0082), // Morado intermedio para el texto
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Botón de gestión de vehículos
            Button(
                onClick = { navController.navigate("vehiculos_disponibles") },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9932CC)), // Morado más claro
                shape = RoundedCornerShape(12.dp)
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Text("Gestión de Vehículos", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botón para ver historial de rentas
            Button(
                onClick = { isDialogOpen = true }, // Abre el cuadro de diálogo
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9932CC)), // Morado más claro
                shape = RoundedCornerShape(12.dp)
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ver Historial de Rentas", fontSize = 16.sp)
            }

            // Cuadro de diálogo para buscar historial de rentas de un vehículo
            if (isDialogOpen) {
                AlertDialog(
                    onDismissRequest = { isDialogOpen = false },
                    title = {
                        Text(
                            "Buscar Vehículo",
                            color = Color(0xFF800080)
                        )
                    }, // Morado fuerte para el título
                    text = {
                        Column {
                            Text(
                                "ID del vehículo:",
                                color = Color(0xFF4B0082)
                            ) // Morado intermedio para el texto
                            OutlinedTextField(
                                value = vehiculoId,
                                onValueChange = { vehiculoId = it },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = Color(0xFF800080), // Morado fuerte
                                    unfocusedBorderColor = Color(0xFF9932CC) // Morado claro
                                )
                            )
                        }
                    },
                    confirmButton = {
                        // Botón para buscar el historial del vehículo ingresado
                        Button(
                            onClick = {
                                isDialogOpen = false
                                if (vehiculoId.isNotEmpty()) {
                                    navController.navigate("vehiculos_rentados_admin/$vehiculoId")
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF800080), // Morado oscuro para el botón de "Buscar"
                                contentColor = Color.White
                            )
                        ) {
                            Text("Buscar")
                        }
                    },
                    dismissButton = {
                        // Botón para cerrar el cuadro de diálogo sin realizar búsqueda
                        Button(
                            onClick = { isDialogOpen = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF800080), // Morado oscuro para el botón de "Cancelar"
                                contentColor = Color.White
                            )
                        ) {
                            Text("Cancelar")
                        }
                    },
                    containerColor = Color.White // Mantener el fondo del cuadro de diálogo en blanco
                )
            }
        }
    }
}
