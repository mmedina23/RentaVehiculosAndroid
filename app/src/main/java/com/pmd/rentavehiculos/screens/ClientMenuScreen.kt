package com.pmd.rentavehiculos.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ClientMenuScreen(
    onVehiculosDisponiblesClick: () -> Unit,
    onHistorialRentadosClick: () -> Unit,
    onLogoutClick: () -> Unit //parámetro para la acción de log out
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onVehiculosDisponiblesClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Vehículos Disponibles")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onHistorialRentadosClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Historial de Vehículos Rentados")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onLogoutClick,  // Se ejecuta cuando se pulsa el botón de Log Out
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Log Out")
        }
    }
}