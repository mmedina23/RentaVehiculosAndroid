package com.pmd.rentavehiculos.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun MenuScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 🔥 Imagen eliminada para evitar errores de referencia

        Text(
            text = "Bienvenido",
            fontSize = 38.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF0077B7),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = { navController.navigate("vehiculos") },
            modifier = Modifier.fillMaxWidth(0.8f),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0077B7)),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Alquilar un Vehículo", fontSize = 16.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { navController.navigate("vehiculos_rentados") },
            modifier = Modifier.fillMaxWidth(0.8f),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0077B7)),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Mis Vehículos Alquilados", fontSize = 16.sp, color = Color.White)
        }
    }
}
