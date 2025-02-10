package com.pmd.rentavehiculos.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pmd.rentavehiculos.ui.auth.LoginScreen
import com.pmd.rentavehiculos.ui.theme.home.HomeScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier // Agrega el parámetro Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = modifier // Aplica el Modifier aquí
    ) {
        composable("login") { LoginScreen(navController) }
        composable("home") { HomeScreen() }
    }
}
