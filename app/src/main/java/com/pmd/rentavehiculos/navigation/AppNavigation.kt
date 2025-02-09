package com.pmd.rentavehiculos.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pmd.rentavehiculos.screen.LoginScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier // Parámetro opcional para el modificador
) {
    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = modifier // Aplicar el modificador recibido
    ) {
        composable("login") {
            LoginScreen(navController = navController)
        }
        composable("patata") {
            PatataScreen()
        }
    }
}

@Composable
fun PatataScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Patata", style = MaterialTheme.typography.headlineLarge)
    }
}
