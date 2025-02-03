package com.pmd.rentavehiculos.navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pmd.rentavehiculos.LoginScreen
import com.pmd.rentavehiculos.MainScreen
import com.pmd.rentavehiculos.SplashScreen


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreens.SplashScreen.ruta
    ) {
        composable(AppScreens.SplashScreen.ruta) {
            SplashScreen(navController)
        }
        composable(AppScreens.LoginScreen.ruta) {
            LoginScreen(navController)
        }
        composable(AppScreens.MainScreen.ruta) {
            MainScreen(navController)
        }
    }
}
