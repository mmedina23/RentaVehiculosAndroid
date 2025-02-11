package com.pmd.rentavehiculos.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuAdminScreen(navController: NavHostController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Menú Administrador") }) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            Button(
                onClick = { navController.navigate("vehiculos_disponibles_admin") },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Text("Vehículos Disponibles")
            }
            Button(
                onClick = { navController.navigate("vehiculos_rentados_admin") },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Text("Vehículos Rentados")
            }
        }
    }
}
