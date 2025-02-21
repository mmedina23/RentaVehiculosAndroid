package com.pmd.rentavehiculos.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pmd.rentavehiculos.R

@Composable
fun MenuScreen(navController: NavController) {
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
            // Nombre de la aplicación con estilo llamativo
            Text(
                text = "DriveGo🚗",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Logo centrado
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo principal",
                modifier = Modifier
                    .width(260.dp)
                    .height(260.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Título "Menú"
            Text(
                text = "Menú Principal",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Botón de alquilar un vehículo
            MenuButton(
                text = "🚘 Alquilar un Vehículo",
                onClick = { navController.navigate("vehiculos") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de mis vehículos alquilados
            MenuButton(
                text = "📋 Mis Vehículos Alquilados",
                onClick = { navController.navigate("vehiculos_rentados") }
            )
        }
    }
}

// Función para los botones con diseño mejorado
@Composable
fun MenuButton(text: String, onClick: () -> Unit) {
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
