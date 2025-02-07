package com.pmd.rentavehiculos.navegacion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay

@Composable
fun AppNavegation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppScreens.SplashScreen.ruta) {
        composable(AppScreens.SplashScreen.ruta) {
            SplashScreen(navController)
        }
        composable(AppScreens.MainList.ruta) {
            MainList(navController, ListaNaves())
        }
        composable(AppScreens.MenuScreen.ruta){
            MenuScreen(navController, ListaNaves())
        }
        composable(AppScreens.MostrarNave.ruta){
            MostrarNave(navController, ListaNaves())
        }
        composable(AppScreens.AddNave.ruta){
            AddNave(ListaNaves(),navController)
        }

    }
}