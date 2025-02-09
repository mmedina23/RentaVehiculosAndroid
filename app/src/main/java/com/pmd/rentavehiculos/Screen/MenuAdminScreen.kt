package com.pmd.rentavehiculos.Screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pmd.rentavehiculos.viewmodel.LoginViewModel
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MenuAdminScreen(navController: NavController, loginViewModel: LoginViewModel = viewModel()) {
    var isDialogOpen by remember { mutableStateOf(false) }
    var vehiculoId by remember { mutableStateOf("") }

    // Fondo degradado moderno
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF0A192F), Color(0xFF1E3A8A), Color(0xFF2563EB))
                )
            )
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título con estilo llamativo
            Text(
                text = "Panel de Administración",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.SansSerif,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Botón para gestionar vehículos
            MenuButton("Gestión de Vehículos", Color(0xFF34D399)) {
                navController.navigate("vehiculos_disponibles")
            }

            // Botón para ver historial de rentas
            MenuButton("Historial de Rentas", Color(0xFF3B82F6)) {
                isDialogOpen = true
            }

            // Cuadro de diálogo para buscar historial de rentas
            if (isDialogOpen) {
                AlertDialog(
                    onDismissRequest = { isDialogOpen = false },
                    containerColor = Color(0xFF1E3A8A),
                    shape = RoundedCornerShape(20.dp),
                    title = {
                        Text(
                            "Buscar Historial",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.White
                        )
                    },
                    text = {
                        Column {
                            Text(
                                "Ingrese el ID del vehículo:",
                                fontSize = 16.sp,
                                color = Color.White
                            )
                            OutlinedTextField(
                                value = vehiculoId,
                                onValueChange = { vehiculoId = it },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
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
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF34D399))
                        ) {
                            Text("Buscar", color = Color.White)
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = { isDialogOpen = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                        ) {
                            Text("Cancelar", color = Color.White)
                        }
                    }
                )
            }
        }
    }
}

// Botón reutilizable con diseño moderno
@Composable
fun MenuButton(text: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .padding(bottom = 14.dp)
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 6.dp)
    ) {
        Text(text, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMenuAdminScreen() {
    MenuAdminScreen(navController = NavController(LocalContext.current))
}
