package com.pmd.rentavehiculos.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pmd.rentavehiculos.R
import com.pmd.rentavehiculos.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicioAdmi(navController: NavController, loginViewModel: LoginViewModel = viewModel()) {
    var isDialogOpen by remember { mutableStateOf(false) } // Controla si el diálogo de historial está abierto
    var vehiculoId by remember { mutableStateOf("") } // Estado para almacenar el ID del vehículo ingresado

    // Contenedor principal con imagen de fondo
    Box(modifier = Modifier.fillMaxSize()) {

        // Imagen de fondo con difuminado
        Image(
            painter = painterResource(id = R.drawable.logo4), // Asegúrate de que la imagen está en res/drawable
            contentDescription = "Fondo de administrador",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        // Capa semitransparente para mejorar la visibilidad del contenido
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)) // Opacidad del fondo
        )

        // Contenido principal de la pantalla
        Scaffold(
            topBar = {
                // Barra superior centrada con color azul
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Panel de Administrador",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color(0xFF0D47A1) // Azul oscuro
                    )
                )
            },
            containerColor = Color.Transparent // Hace que el fondo de Scaffold sea transparente
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                // Texto de bienvenida
                Text(
                    text = "Bienvenido, Administrador",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Botón para gestionar vehículos
                Button(
                    onClick = { navController.navigate("vehiculos_disponibles") },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0077B7)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Gestión de Vehículos", fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Botón para ver historial de rentas
                Button(
                    onClick = { isDialogOpen = true },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0077B7)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Ver Historial de Rentas", fontSize = 16.sp)
                }

                // Diálogo para ingresar ID del vehículo y buscar su historial de rentas
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
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
}

// Vista previa para verificar el diseño en el editor de Jetpack Compose
@Preview(showBackground = true)
@Composable
fun PreviewPantallaInicioAdmi() {
    PantallaInicioAdmi(navController = object : NavController(LocalContext.current) {})
}
