package com.pmd.rentavehiculos.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pmd.rentavehiculos.screen.LoginScreen

@Composable
fun AppNavigation(modifier: Modifier) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "login") {
        composable("login") {
            LoginScreen { role ->
                navController.navigate(if (role == "admin") "adminMenu" else "clienteMenu")
            }
        }
        composable("adminMenu") { AdminMenuScreen() }
        composable("clienteMenu") { ClienteMenuScreen() }
    }
}

@Composable
fun ClienteMenuScreen() {
    TODO("Not yet implemented")
}

@Composable
fun AdminMenuScreen() {
    TODO("Not yet implemented")
}
