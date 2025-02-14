package com.pmd.rentavehiculos.navigation

import AdminHomeScreen
import android.content.Context
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pmd.rentavehiculos.ui.auth.LoginScreen
import com.pmd.rentavehiculos.ui.theme.admin.ListaVehiculosDisponibles
import com.pmd.rentavehiculos.ui.theme.admin.ListaVehiculosRentados
import com.pmd.rentavehiculos.ui.theme.home.HomeScreen


@Composable
fun AppNavigation(
    navController: NavHostController,
    context: Context,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = modifier
    ) {
        composable("login") {
            LoginScreen(
                navController = navController,
                context = context // Pasamos el contexto
            )
        }
        composable("home") { HomeScreen() }

        composable(
            route = "admin_home",
        ) {
            AdminHomeScreen(
                navController = navController,
                context = context
            )
        }

        composable("vehiculos_disponibles") {
            ListaVehiculosDisponibles(navController = navController, context = context)
        }
        composable("vehiculos_rentados") {
            ListaVehiculosRentados(navController = navController, context = context)
        }




    }
}
