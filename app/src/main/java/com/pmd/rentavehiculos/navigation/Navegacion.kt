package com.pmd.rentavehiculos.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun Navegacion() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = appPantallas.splashScreen.ruta
    ) {
        composable(appPantallas.splashScreen.ruta) {
            SplahScreen(navController)
        }
        composable(appPantallas.LoginScreen.ruta) {
            LoginScreen(navController)
        }
        composable(appPantallas.ClientScreen.ruta) {
            ClientScreen(navController)
        }
        composable(appPantallas.AdminScreen.ruta) {
            AdminScreen(navController)
        }
        composable(appPantallas.VehiculoScreen.ruta) {
            VehiculoScreen(navController)
        }
        composable(appPantallas.PersonaScreen.ruta) {
            PersonaScreen(navController)
        }


    }
}