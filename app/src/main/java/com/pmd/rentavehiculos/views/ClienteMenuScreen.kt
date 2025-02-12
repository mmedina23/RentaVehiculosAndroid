package com.pmd.rentavehiculos.views

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteMenuScreen(navController: NavHostController, token: String, personaId: Int) {  // ✅ Añadimos `personaId`
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Bienvenido Cliente", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            navController.navigate("vehiculos/$token/$personaId") // ✅ Ahora pasamos personaId
        }) {
            Text("Ver Vehículos Disponibles")
        }


        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            navController.navigate("vehiculos_rentados/$token/$personaId") // ✅ Ahora pasamos `personaId`
        }) {
            Text("Ver Vehículos Rentados")
        }
    }
}
