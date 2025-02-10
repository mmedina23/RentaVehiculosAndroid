package com.pmd.rentavehiculos.Core

import PersonasFunction
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.pmd.rentavehiculos.Screens.AdmindScreen
import com.pmd.rentavehiculos.Screens.ClienteScreen
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
            LoginFuncion(
                NavigationClient =  {token -> navController.navigate(Cliente(token = token))},
                NavigationAdmind = {token -> navController.navigate(Admind(token = token))}
            )
        }

        composable<Cliente> { backStackEntry ->
            val cliente:Cliente = backStackEntry.toRoute()
            ClienteScreen(token = cliente.token, NavigationVehiculosRentados = {}, NavigationCarList = {})
        }

        composable<Admind> { backStackEntry ->
            val admind:Admind = backStackEntry.toRoute()
            AdmindScreen(token = admind.token)
        }
    }
}