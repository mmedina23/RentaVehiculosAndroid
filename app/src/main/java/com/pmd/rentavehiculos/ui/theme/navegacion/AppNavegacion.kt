package com.pmd.rentavehiculos.ui.theme.navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pmd.rentavehiculos.ui.theme.splash.SplashScreen
import com.pmd.rentavehiculos.ui.theme.login.LoginScreen // Agregar pantalla login cuando la implementemos

@Composable
fun AppNavegacion() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController) } // Luego creamos LoginScreen
    }

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController) }
        // Aquí luego agregaremos "home" y "register"
    }
}

@Composable
fun NavHost(
    navController: rememberNavController,
    startDestination: String,
    content: @Composable () -> composable
) {
    TODO("Not yet implemented")
}