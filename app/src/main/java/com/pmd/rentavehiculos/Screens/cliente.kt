package com.pmd.rentavehiculos.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun cliente(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Cliente") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { navController.navigate("listaVehiculos") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver Vehículos Disponibles")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("rentarVehiculo") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Mis vehículos rentados")
            }
        }
    }
}
