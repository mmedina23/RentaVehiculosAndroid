package com.pmd.rentavehiculos.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pmd.rentavehiculos.R

@Composable
fun MenuScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.npc),
            contentDescription = "Logo principal",
            modifier = Modifier
                .width(300.dp)
                .height(410.dp)         )

        Text(
            text = "Bienvenido",
            fontSize = 38.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.Serif,
            color = Color(0xFF0077B7),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = { navController.navigate("vehiculos") },
            modifier = Modifier.fillMaxWidth(0.8f),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0077B7),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Alquilar un Vehículo", fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { navController.navigate("vehiculos_rentados") },
            modifier = Modifier.fillMaxWidth(0.8f),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0077B7), // Azul RGB 0,119,183
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Mis Vehículos Alquilados", fontSize = 16.sp)
        }
    }
}


