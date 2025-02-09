package com.pmd.rentavehiculos.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

@Composable
fun MenuScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF081C24), Color(0xFF1B3B5A), Color(0xFF2A628F))
                )
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título con un diseño más sofisticado
            Text(
                text = "Encuentra tu vehículo ideal en segundos",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = Color.White,
                modifier = Modifier.padding(bottom = 30.dp)
            )

            // Botones con colores renovados
            MenuButtonCliente("Explorar Vehículos", Color(0xFF00C897)) {
                navController.navigate("vehiculos")
            }

            MenuButtonCliente("Historial de Alquileres", Color(0xFF007BFF)) {
                navController.navigate("vehiculos_rentados")
            }

            MenuButtonCliente("Soporte y Ayuda", Color(0xFFFF6F61)) {
                navController.navigate("soporte")
            }

            // Mensaje final con mejor presentación
            Text(
                text = "¿Tienes preguntas? ¡Contáctanos!",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.Default,
                color = Color(0xFFB0E0E6),
                modifier = Modifier.padding(top = 25.dp)
            )
        }
    }
}

// Componente reutilizable para los botones
@Composable
fun MenuButtonCliente(text: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .padding(vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 8.dp)
    ) {
        Text(text, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMenuScreen() {
    MenuScreen(navController = rememberNavController())
}
