package com.pmd.rentavehiculos.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
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
import com.pmd.rentavehiculos.vista.VistaLogin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuAdminScreen(navController: NavController, loginViewModel: VistaLogin = viewModel()) {
    var isDialogOpen by remember { mutableStateOf(false) }
    var vehiculoId by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0077B7), Color(0xFF003366))))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título de la aplicación
            Text(
                text = "DriveGo Admin🚗",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Imagen de Administrador
            Image(
                painter = painterResource(id = R.drawable.npc),
                contentDescription = "Imagen de Administrador",
                modifier = Modifier
                    .size(250.dp)
                    .shadow(8.dp, shape = RoundedCornerShape(20.dp))
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Texto de bienvenida
            Text(
                text = "Bienvenido, Administrador",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Botón de Gestión de Vehículos
            AdminButton(
                text = "🚘 Gestión de Vehículos",
                onClick = { navController.navigate("vehiculos_disponibles") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de Historial de Rentas
            AdminButton(
                text = "📋 Ver Historial de Rentas",
                onClick = { isDialogOpen = true }
            )

            // Diálogo para buscar vehículo por ID
            if (isDialogOpen) {
                AlertDialog(
                    onDismissRequest = { isDialogOpen = false },
                    title = { Text("Buscar vehículo") },
                    text = {
                        Column {
                            Text("ID del vehículo:", color = Color.Black)
                            OutlinedTextField(
                                value = vehiculoId,
                                onValueChange = { vehiculoId = it },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = Color(0xFF0077B7),
                                    unfocusedBorderColor = Color(0xFF0077B7).copy(alpha = 0.5f),
                                    cursorColor = Color(0xFF0077B7)
                                )
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
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0077B7),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Buscar")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = { isDialogOpen = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0077B7),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Cancelar")
                        }
                    },
                    containerColor = Color.White
                )
            }
        }
    }
}

// Función para los botones con diseño mejorado
@Composable
fun AdminButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(52.dp)
            .shadow(8.dp, shape = RoundedCornerShape(12.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF00A86B),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}
