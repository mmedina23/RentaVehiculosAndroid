package com.pmd.rentavehiculos.data.navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pmd.rentavehiculos.data.screens.SplashScreen
import com.pmd.rentavehiculos.data.screens.LogInScreen

// Define AppScreens with routes
sealed class AppScreens(val ruta: String) {
    object SplashScreen : AppScreens("splash_screen")
    object LogInScreen : AppScreens("login_screen")

}

@Composable
fun AppNavegation() {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = AppScreens.SplashScreen.ruta) {
        composable(route = AppScreens.SplashScreen.ruta) {
            SplashScreen(navController)
        }

        composable(route = AppScreens.LogInScreen.ruta) {
            AppScreens.LogInScreen(navController)
        }


    }
}
