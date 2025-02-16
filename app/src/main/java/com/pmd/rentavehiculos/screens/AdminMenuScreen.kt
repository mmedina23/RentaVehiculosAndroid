package com.pmd.rentavehiculos.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pmd.rentavehiculos.R

@Composable
fun AdminMenuScreen(
    onVerDisponibles: () -> Unit,
    onVerRentados: () -> Unit,
   // onGestionVehiculos: () -> Unit,  // para CRUD de vehículos si se implementa
    onLogoutClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen de fondo para el menú de administración
        Image(
            painter = painterResource(id = R.drawable.adminmenu1),
            contentDescription = "Fondo del menú de administración",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Contenedor de los elementos (botones)
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .offset(x = (-45).dp, y = 170.dp)
                .padding(horizontal = 64.dp, vertical = 48.dp)
                .background(Color.White.copy(alpha = 0.90f), shape = RoundedCornerShape(16.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
//            Text(
//                text = "Panel de",
//                style = MaterialTheme.typography.titleLarge,
//                modifier = Modifier.fillMaxWidth(),
//                textAlign = TextAlign.Center,
//                fontWeight = FontWeight.ExtraBold
//            )
//            Text(
//                text = "Administración",
//                style = MaterialTheme.typography.titleLarge,
//                modifier = Modifier.fillMaxWidth(),
//                textAlign = TextAlign.Center,
//                color = Color(0xFFFFC107),
//                fontWeight = FontWeight.ExtraBold
//            )
//            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = onVerDisponibles,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "Vehículos Disponibles",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
            Button(
                onClick = onVerRentados,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "Vehículos Rentados",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = onLogoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,  // Fondo negro
                    contentColor = Color.White       // Texto blanco
                )
            ) {
                Text(
                    text = "Cerrar Sesión",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}