package com.pmd.rentavehiculos.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pmd.rentavehiculos.screen.Admin
import com.pmd.rentavehiculos.screen.AdminListado
import com.pmd.rentavehiculos.screen.Client
import com.pmd.rentavehiculos.screen.LoginScreen

@Composable
fun AppNavigation(
    navController: NavHostController, // Debe ser NavHostController
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = modifier
    ) {
        composable("login") {
            LoginScreen(navController = navController)
        }
        composable("admin") {
            Admin(navController = navController)
        }
        composable("adminListado") {
            AdminListado()
        }
        composable("cliente") {
            Client(navController = navController)
        }
    }
}
