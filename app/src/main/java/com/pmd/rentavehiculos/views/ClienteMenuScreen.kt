package com.pmd.rentavehiculos.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteMenuScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Menú Cliente") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { navController.navigate("vehiculos") },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text("Ver Vehículos Disponibles")
            }

            Button(
                onClick = { navController.navigate("vehiculos_rentados") },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text("Ver Mis Vehículos Rentados")
            }
        }
    }
}
