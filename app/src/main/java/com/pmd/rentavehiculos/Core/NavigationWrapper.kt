package com.pmd.rentavehiculos.Core

import PersonasFunction
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pmd.rentavehiculos.Screens.LoginFuncion
import com.pmd.rentavehiculos.Screens.SplashScreen as splashScreenFunction


@Composable
fun NavigationWrapper() {
    val navController = rememberNavController();

    NavHost(navController = navController, startDestination = SplashScreen){
        composable<SplashScreen> {
            splashScreenFunction(navController)
        }

        composable<Login> {
            LoginFuncion()
        }

        composable<Personas> {
            PersonasFunction()
        }
    }
}