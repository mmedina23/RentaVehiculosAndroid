package com.pmd.rentavehiculos.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
@Composable
fun AdminMenuScreen(
    onVerDisponibles: () -> Unit,
    onVerRentados: () -> Unit,
    onGestionVehiculos: () -> Unit,  // para CRUD de vehículos
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Panel de Administración", style = MaterialTheme.typography.titleLarge)
        Button(onClick = onVerDisponibles) {
            Text("Ver Vehículos Disponibles")
        }
        Button(onClick = onVerRentados) {
            Text("Ver Vehículos Rentados")
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onLogoutClick) {
            Text("Cerrar Sesión")
        }
    }
}