package com.pmd.rentavehiculos.data.navegacion

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pmd.rentavehiculos.Screen.MainScreen
import com.pmd.rentavehiculos.Screen.SplashScreen

@Composable
fun AppNavegacion(innerPadding: PaddingValues) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppScreens.SplashScreen.ruta
    ) {
        composable(AppScreens.SplashScreen.ruta) {
            SplashScreen(navController)
        }
        composable(AppScreens.MainScreen.ruta) {
            MainScreen()
        }
    }
}