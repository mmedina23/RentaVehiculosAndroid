package com.pmd.rentavehiculos.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import com.pmd.rentavehiculos.R

@Composable
fun ClientMenuScreen(
    onVehiculosDisponiblesClick: () -> Unit,
    onHistorialRentadosClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.clientemenu),
            contentDescription = "Fondo del menú de cliente",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

       // Contenedor de los botones
        Column(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = 90.dp)
                .padding(horizontal = 82.dp, vertical = 48.dp),
           // verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onVehiculosDisponiblesClick,
                modifier = Modifier.fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "Vehículos Disponibles",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onHistorialRentadosClick,
                modifier = Modifier.fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "Vehículos Rentados",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onLogoutClick,
                modifier = Modifier.fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,  // Fondo negro
                    contentColor = Color.White       // Texto blanco
                )
            ) {
                Text(
                    text = "Log Out",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
